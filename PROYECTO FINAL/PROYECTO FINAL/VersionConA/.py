import random
import math
import osmnx as ox 
import networkx as nx
from shapely.geometry import Polygon
import folium

# DELIMITACIÓN Y DESCARGA DEL MAPA (Bogotá Norte) (10Km de perimetro Barrios Colina, Mazuren, Cedro, Prado)
vertices = [
    (-74.06529659245139, 4.744539445793631), (-74.06538242124677, 4.744068981962705),(-74.06639094796563, 4.7438979041318206),
    (-74.06632656430796, 4.742892825396139),(-74.06604760824129, 4.742636210455151),(-74.06546824162022, 4.742400980682175),
    (-74.06585446536491, 4.739514073563151),(-74.06645527229844, 4.736670015156694),(-74.06737795388963, 4.73339814640188),
    (-74.06864395063242, 4.730297372332393),(-74.06956662463337, 4.7283941365579265),(-74.071519269736, 4.726105956617533),
    (-74.06712811655902, 4.724327152456083),(-74.06660500570523, 4.723856680555733),(-74.06608189508796, 4.723589655305674),
    (-74.0655460251428, 4.723697735736931),(-74.0651377425747, 4.723557866722073),(-74.06175027908299, 4.7221146589755065),
    (-74.0611952709094, 4.7217014048386865),(-74.06058284854457, 4.721135563763555),(-74.05997042562099, 4.72074773855847),
    (-74.05886678857097, 4.720035670725708),(-74.05822884800823, 4.719698709419759),(-74.05723366080426, 4.719349030378438),
    (-74.05641709527012, 4.719164652107858),(-74.05513483711992, 4.718980280870684),(-74.05423534111414, 4.718891270845181),
    (-74.05420345238655, 4.718923054635479),(-74.05327205255587, 4.718497086124799),(-74.05244910716596, 4.717982107747878),
    (-74.05157512919703, 4.717994824670751),(-74.05003769325147, 4.718471657176264),(-74.04892767496064, 4.718052039338339),
    (-74.0473583440069, 4.717543420552625),(-74.0433520782871, 4.716112915212934),(-74.04282896643188, 4.715966686524542),
    (-74.03683050253724, 4.713766035915589),(-74.03447012351342, 4.7131620416010565),(-74.033238898126, 4.712869582421474),
    (-74.03280509893477, 4.712678847239931),(-74.0324606112557, 4.712640699833421),(-74.03033626922658, 4.711826896825026),
    (-74.02887538639705, 4.7114327083561145),(-74.0287620405603, 4.713785414712032),(-74.02841305923648, 4.716068766007889),
    (-74.02837513113747, 4.717112147055419),(-74.02668335591406, 4.720136434932524),(-74.02608402828082, 4.7211495680867985),
    (-74.02597781916992, 4.722336596591093),(-74.02593229954432, 4.723206074259927),(-74.02566677266476, 4.72387897444851),
    (-74.0245515683734, 4.7258523030509),(-74.02431638204219, 4.7278331956281034),(-74.03884745750781, 4.734863912935852),
    (-74.039791596748, 4.735238143721666),(-74.04294026591789, 4.736709797900004),(-74.04699599842573, 4.737598159885746),
    (-74.04807961094978, 4.738496306691121),(-74.0492490548929, 4.738795689135676),(-74.05071890495915, 4.738314540400335),
    (-74.05115231343014, 4.738473151593268),(-74.0605859262118, 4.7408522672448425),(-74.0609721639842, 4.741312030379007),
    (-74.06156225148672, 4.741247878834419),(-74.0648443746398, 4.742690261485869),(-74.06491949660897, 4.744016113378263),
    (-74.06529659245139, 4.744539445793631)
]
poligono = Polygon(vertices)
G = ox.graph_from_polygon(poligono, network_type="drive")

# MODELO DE CONGESTIÓN, RECIBIR VELOCIDADES MAXIMAS DE LAS CALLES
gdf_edges = ox.graph_to_gdfs(G, nodes=False, edges=True)

def calcular_velocidad_promedio_bogota(row):
    tipo_via = row.get('highway', 'residential')
    if tipo_via in ['primary', 'trunk', 'secondary']:
        max_speed = 50.0
    else:
        max_speed = 30.0
        
    # Aplicar congestión simulada real
    if tipo_via in ['primary', 'trunk']:
        return max_speed * 0.30  # ~15 km/h (Avenidas trancadas - ROJO)
    elif tipo_via in ['secondary', 'tertiary']:
        return max_speed * 0.50  # ~25 km/h (Flujo moderado - AMARILLO)
    else:
        return max_speed * 0.90  # ~27 km/h (Barrios libres - VERDE)

# Aplicar las velocidades y calcular el tiempo real de cruce en segundos
gdf_edges['speed_kph'] = gdf_edges.apply(calcular_velocidad_promedio_bogota, axis=1)
gdf_edges['travel_time'] = gdf_edges['length'] / (gdf_edges['speed_kph'] / 3.6)

#Reinyectar los datos actualizados de tráfico al Grafo G
G = ox.graph_from_gdfs(ox.graph_to_gdfs(G, edges=False), gdf_edges)

# ==========================================================
#               ALGORITMO A*
# ==========================================================
# ----------------------------------------------------------
# Heurística Haversine
# ----------------------------------------------------------

def heuristica(nodo_actual, nodo_destino):

    # Coordenadas del nodo actual
    lat1 = G.nodes[nodo_actual]["y"]
    lon1 = G.nodes[nodo_actual]["x"]

    # Coordenadas del destino
    lat2 = G.nodes[nodo_destino]["y"]
    lon2 = G.nodes[nodo_destino]["x"]

    # Radio de la Tierra en metros
    radio = 6371000

    # Conversión a radianes
    phi1 = math.radians(lat1)
    phi2 = math.radians(lat2)

    delta_phi = math.radians(lat2 - lat1)
    delta_lambda = math.radians(lon2 - lon1)

    # Fórmula Haversine
    a = (
        math.sin(delta_phi / 2) ** 2
        + math.cos(phi1)
        * math.cos(phi2)
        * math.sin(delta_lambda / 2) ** 2
    )

    c = 2 * math.atan2(math.sqrt(a), math.sqrt(1 - a))

    distancia = radio * c

    return distancia

# ----------------------------------------------------------
# Calcular la mejor ruta usando A*
# ----------------------------------------------------------

def calcular_ruta_astar(origen, destino):

    try:

        # Calcular la mejor ruta
        ruta = nx.astar_path(
            G,
            origen,
            destino,
            heuristic=heuristica,
            weight="travel_time"
        )

        # Tiempo total de la ruta
        tiempo_total = nx.path_weight(
            G,
            ruta,
            weight="travel_time"
        )

        # Distancia total recorrida
        distancia_total = nx.path_weight(
            G,
            ruta,
            weight="length"
        )

        return {
            "ruta": ruta,
            "tiempo": tiempo_total,
            "distancia": distancia_total,
            "existe": True
        }

    except nx.NetworkXNoPath:

        return {
            "ruta": [],
            "tiempo": float("inf"),
            "distancia": float("inf"),
            "existe": False
        }
    
# ----------------------------------------------------------
# Buscar automáticamente la mejor estación
# ----------------------------------------------------------

def buscar_mejor_estacion(destino):

    estaciones = [
        nodo_estacion_1,
        nodo_estacion_2,
        nodo_estacion_3,
        nodo_estacion_4
    ]

    mejor_estacion = None
    mejor_resultado = None

    print("\n")
    print("=" * 60)
    print("          ANÁLISIS DE ESTACIONES")
    print("=" * 60)

    for estacion in estaciones:

        resultado = calcular_ruta_astar(estacion, destino)

        if resultado["existe"]:

            tiempo = resultado["tiempo"] / 60
            distancia = resultado["distancia"] / 1000

            print(f"\n{puntosInteres[estacion]['nombre']}")
            print(f"Tiempo: {tiempo:.2f} minutos")
            print(f"Distancia: {distancia:.2f} km")

            if (mejor_resultado is None or
                    resultado["tiempo"] < mejor_resultado["tiempo"]):

                mejor_estacion = estacion
                mejor_resultado = resultado

    print("\n" + "=" * 60)
    print("MEJOR ESTACIÓN SELECCIONADA")
    print("=" * 60)
    print(puntosInteres[mejor_estacion]["nombre"])
    print("=" * 60)

    return mejor_estacion, mejor_resultado

# UBICACIÓN DE HOSPITALES Y ESTACIONES
coordenadas_manuales = {
    # Estaciones de policía
    "estacion1": (-74.04681193729564, 4.720611460046508),
    "estacion2": (-74.06056333389323, 4.726892681568199),
    "estacion3": (-74.05798928908166, 4.735977868888742),
    "estacion4": (-74.03079644952695, 4.718723118530826),

    # Hospitales
    "hospital1": (-74.04653875922429, 4.72008969821011),
    "hospital2": (-74.03778628785662, 4.730907698448973),
    "hospital3": (-74.02853954236205, 4.720846365081989),
    "hospital4": (-74.05006571079524, 4.736707535948786),
    "hospital5": (-74.05598955918100, 4.73383996180555),
}

# Encontrar los nodos más cercanos reales en el mapa
nodo_estacion_1 = ox.nearest_nodes(G, X=coordenadas_manuales["estacion1"][0], Y=coordenadas_manuales["estacion1"][1])
nodo_estacion_2 = ox.nearest_nodes(G, X=coordenadas_manuales["estacion2"][0], Y=coordenadas_manuales["estacion2"][1])
nodo_estacion_3 = ox.nearest_nodes(G, X=coordenadas_manuales["estacion3"][0], Y=coordenadas_manuales["estacion3"][1])
nodo_estacion_4 = ox.nearest_nodes(G, X=coordenadas_manuales["estacion4"][0], Y=coordenadas_manuales["estacion4"][1])

nodo_hospital_1 = ox.nearest_nodes(G, X=coordenadas_manuales["hospital1"][0], Y=coordenadas_manuales["hospital1"][1])
nodo_hospital_2 = ox.nearest_nodes(G, X=coordenadas_manuales["hospital2"][0], Y=coordenadas_manuales["hospital2"][1])
nodo_hospital_3 = ox.nearest_nodes(G, X=coordenadas_manuales["hospital3"][0], Y=coordenadas_manuales["hospital3"][1])
nodo_hospital_4 = ox.nearest_nodes(G, X=coordenadas_manuales["hospital4"][0], Y=coordenadas_manuales["hospital4"][1])
nodo_hospital_5 = ox.nearest_nodes(G, X=coordenadas_manuales["hospital5"][0], Y=coordenadas_manuales["hospital5"][1])

# Excluir los puntos fijos del muestreo aleatorio
nodos_ocupados = [
    nodo_estacion_1, nodo_estacion_2, nodo_estacion_3,
    nodo_estacion_4, nodo_hospital_1, nodo_hospital_2, 
    nodo_hospital_3, nodo_hospital_4, nodo_hospital_5
]

todos_los_nodos = list(G.nodes())
nodos_disponibles = [n for n in todos_los_nodos if n not in nodos_ocupados]
nodos_vehiculos = random.sample(nodos_disponibles, 4)
nodos_emergencias = random.sample(nodos_disponibles, 5)

nodo_ambulancia_1 = nodos_vehiculos[0]
nodo_ambulancia_2 = nodos_vehiculos[1]
nodo_patrulla_1   = nodos_vehiculos[2]
nodo_patrulla_2   = nodos_vehiculos[3]
nodo_emergencia_1 = nodos_emergencias [0]
nodo_emergencia_2 = nodos_emergencias [1]
nodo_emergencia_3 = nodos_emergencias [2]
nodo_emergencia_4 = nodos_emergencias [3]
nodo_emergencia_5 = nodos_emergencias [4]

puntosInteres = {
    # Estaciones
    nodo_estacion_1: {"nombre": "Estación de Policía 1", "tipo": "estacion"},
    nodo_estacion_2: {"nombre": "Estación de Policía 2", "tipo": "estacion"},
    nodo_estacion_3: {"nombre": "Estación de Policía 3", "tipo": "estacion"},
    nodo_estacion_4: {"nombre": "Estación de Policía 4", "tipo": "estacion"},

    # Hospitales
    nodo_hospital_1: {"nombre": "Hospital 1", "tipo": "hospital"},
    nodo_hospital_2: {"nombre": "Hospital 2", "tipo": "hospital"},
    nodo_hospital_3: {"nombre": "Hospital 3", "tipo": "hospital"},
    nodo_hospital_4: {"nombre": "Hospital 4", "tipo": "hospital"},
    nodo_hospital_5: {"nombre": "Hospital 5", "tipo": "hospital"},

    # Emergencias
    nodo_emergencia_1: {"nombre": "Punto Crítico: Zona de Comercio", "tipo": "emergencia"},
    nodo_emergencia_2: {"nombre": "Punto Crítico: Colegio", "tipo": "emergencia"},
    nodo_emergencia_3: {"nombre": "Punto Crítico: Apartamentos", "tipo": "emergencia"},
    nodo_emergencia_4: {"nombre": "Punto Crítico: Parque", "tipo": "emergencia"},
    nodo_emergencia_5: {"nombre": "Punto Crítico: Choque Vial", "tipo": "emergencia"},

    # Vehículos móviles
    nodo_ambulancia_1: {"nombre": "Ambulancia Alfa (Móvil)", "tipo": "ambulancia"},
    nodo_ambulancia_2: {"nombre": "Ambulancia Beta (Móvil)", "tipo": "ambulancia"},
    nodo_patrulla_1:   {"nombre": "Patrulla 101 (Móvil)", "tipo": "patrulla"},
    nodo_patrulla_2:   {"nombre": "Patrulla 102 (Móvil)", "tipo": "patrulla"},
}

for nodo_id, informacion in puntosInteres.items():
    G.nodes[nodo_id]['nombre'] = informacion['nombre']
    G.nodes[nodo_id]['tipo'] = informacion['tipo']

# ==========================================================
# SISTEMA DE SELECCIÓN DE EMERGENCIAS
# ==========================================================

emergencias = {
    1: nodo_emergencia_1,
    2: nodo_emergencia_2,
    3: nodo_emergencia_3,
    4: nodo_emergencia_4,
    5: nodo_emergencia_5
}

print("\n")
print("=" * 60)
print("      SISTEMA DE RESPUESTA A EMERGENCIAS")
print("=" * 60)

while True:

    print("\nSeleccione una opción:")
    print("1. Emergencia aleatoria")
    print("2. Elegir emergencia manualmente")

    opcion = input("\nOpción: ")

    # ---------------------------------------------
    # Emergencia aleatoria
    # ---------------------------------------------
    if opcion == "1":

        destino = random.choice(list(emergencias.values()))
        break

    # ---------------------------------------------
    # Selección manual
    # ---------------------------------------------
    elif opcion == "2":

        while True:

            print("\nEmergencias disponibles:\n")

            print("1. Zona de Comercio")
            print("2. Colegio")
            print("3. Apartamentos")
            print("4. Parque")
            print("5. Choque Vial")

            try:

                seleccion = int(input("\nSeleccione una emergencia: "))

                if seleccion in emergencias:

                    destino = emergencias[seleccion]
                    break

                else:

                    print("\n Debe ingresar un número entre 1 y 5.")

            except ValueError:

                print("\n Debe ingresar un número válido.")

        break

    else:

        print("\n Opción inválida. Intente nuevamente.")

# ==========================================================
# BUSCAR LA MEJOR ESTACIÓN PARA LA EMERGENCIA SELECCIONADA
# ==========================================================

mejor_estacion, resultado = buscar_mejor_estacion(destino)

ruta = resultado["ruta"]

# Mostrar resultados
print("\n")
print("=" * 60)
print("        SISTEMA DE RESPUESTA A EMERGENCIAS")
print("=" * 60)

print("\nEmergencia detectada:")
print(" ", puntosInteres[destino]["nombre"])

print("\nEstación seleccionada:")
print(" ", puntosInteres[mejor_estacion]["nombre"])

print("\nTiempo estimado:")
print(f" {resultado['tiempo']/60:.2f} minutos")

print("\nDistancia:")
print(f" {resultado['distancia']/1000:.2f} km")

print("\nCantidad de nodos recorridos:")
print(" ", len(resultado["ruta"]))

# CREACIÓN DEL MAPA INTERACTIVO (Folium con capas de tráfico)
lat_centro, lon_centro = (4.726734023564359, -74.04575503796416)
mapa_interactivo = folium.Map(location=[lat_centro, lon_centro], zoom_start=15, tiles="openstreetmap")

# Pintar las vías usando los colores del tráfico de Bogotá simulado
gdf_edges_updated = ox.graph_to_gdfs(G, nodes=False, edges=True)

for _, row in gdf_edges_updated.iterrows():
    if row['geometry'].geom_type == 'LineString':
        coordenadas_calle = [(lat, lon) for lon, lat in row['geometry'].coords]
        
        velocidad = row.get('speed_kph', 30)
        # Clasificación visual
        if velocidad <= 16:
            color_trafico = '#E74C3C'  # Rojo: Avenidas colapsadas por trancón
        elif velocidad <= 26:
            color_trafico = '#F1C40F'  # Amarillo: Flujo moderado
        else:
            color_trafico = '#2ECC71'  # Verde: Vías de barrio libres (30 km/h estables)
            
        folium.PolyLine(
            locations=coordenadas_calle,
            color=color_trafico,
            weight=3.5,
            opacity=0.8,
            popup=f"Calle: {row.get('name', 'Sin nombre')}<br>Velocidad Promedio: {velocidad:.1f} km/h"
        ).add_to(mapa_interactivo)

# Agregar los Marcadores de Estaciones, Hospitales y Emergencias
for nodo_id, info in puntosInteres.items():
    lat = G.nodes[nodo_id]['y']
    lon = G.nodes[nodo_id]['x']

    if info['tipo'] == 'estacion':
        color_marcador = 'darkblue'
        icono = 'shield'
        prefix = 'fa'

    elif info['tipo'] == 'hospital':
        color_marcador = 'lightred'
        icono = 'hospital'
        prefix = 'fa'

    elif info['tipo'] == 'emergencia':
        color_marcador = 'darkred'
        icono = 'exclamation-triangle'
        prefix = 'fa'

    elif info['tipo'] == 'ambulancia':
        color_marcador = 'orange'
        icono = 'ambulance'
        prefix = 'fa'

    elif info['tipo'] == 'patrulla':
        color_marcador = 'darkgreen'
        icono = 'car'
        prefix = 'fa'

    else:
        color_marcador = 'blue'
        icono = 'map-marker'
        prefix = 'fa'

    folium.Marker(
        location=[lat, lon],
        popup=f"<b>{info['nombre']}</b><br>Tipo: {info['tipo'].upper()}",
        icon=folium.Icon(
            color=color_marcador,
            icon=icono,
            prefix=prefix
        )
    ).add_to(mapa_interactivo)

    # ==========================================================
# DIBUJAR LA RUTA CALCULADA POR A*
# ==========================================================

coordenadas_ruta = []

for nodo in ruta:

    lat = G.nodes[nodo]["y"]
    lon = G.nodes[nodo]["x"]

    coordenadas_ruta.append((lat, lon))


folium.PolyLine(

    locations=coordenadas_ruta,

    color="blue",

    weight=7,

    opacity=0.9,

    tooltip="Ruta óptima A*"

).add_to(mapa_interactivo)

# ----------------------------------------------------------
# Resaltar estación seleccionada
# ----------------------------------------------------------

folium.CircleMarker(

    location=(
        G.nodes[mejor_estacion]["y"],
        G.nodes[mejor_estacion]["x"]
    ),

    radius=10,

    color="green",

    fill=True,

    fill_color="green",

    popup="Estación seleccionada"

).add_to(mapa_interactivo)


# ----------------------------------------------------------
# Resaltar emergencia
# ----------------------------------------------------------

folium.CircleMarker(

    location=(
        G.nodes[destino]["y"],
        G.nodes[destino]["x"]
    ),

    radius=10,

    color="red",

    fill=True,

    fill_color="red",

    popup="Emergencia"

).add_to(mapa_interactivo)

# Se guarda el mapa modificado en html
mapa_interactivo.save("mapa_trafico_emergencias_bogota.html")
print("¡Mapa actualizado!")

# VISTA CLÁSICA ESTÁTICA ACTUALIZADA
colores_nodos_estaticos = []
tamaños_nodos_estaticos = []

# Se recorren todos los nodos del grafo para asignarles su estilo clásico
for nodo in G.nodes():
    if nodo in puntosInteres:
        tipo = puntosInteres[nodo]['tipo']
        if tipo == 'estacion':
            colores_nodos_estaticos.append("#00008B")   # Azul oscuro para la estación
            tamaños_nodos_estaticos.append(100)
        elif tipo == 'hospital':
            colores_nodos_estaticos.append('#FF474C')   # Rojo para hospitales
            tamaños_nodos_estaticos.append(100)
        elif tipo == 'emergencia':
            colores_nodos_estaticos.append('#8B0000')   # Rojo oscuro para emergencias críticas
            tamaños_nodos_estaticos.append(110)         # Un poco más grande para resaltar
        elif tipo == 'ambulancia':
            colores_nodos_estaticos.append('#F39C12')   # Naranja para ambulancias móviles
            tamaños_nodos_estaticos.append(80)
        elif tipo == 'patrulla':
            colores_nodos_estaticos.append('#006400')   # Azul claro para patrullas móviles
            tamaños_nodos_estaticos.append(80)
    else:
        # Calles e intersecciones normales del barrio
        colores_nodos_estaticos.append('#BDC3C7')       # Gris claro para los nodos normales
        tamaños_nodos_estaticos.append(15)

# Dibujar grafo clasico
colores_aristas = []
grosores_aristas = []

for u, v, k, data in G.edges(keys=True, data=True):
    velocidad = data.get('speed_kph', 30)
    
    # Clasificación idéntica a la del otro mapa para mantener coherencia
    if velocidad <= 16:
        colores_aristas.append('#E74C3C')
        grosores_aristas.append(2.5)
    elif velocidad <= 26:
        colores_aristas.append('#F1C40F')
        grosores_aristas.append(1.8)
    else:
        colores_aristas.append('#2ECC71')
        grosores_aristas.append(1.2)

print("Generando vista de red con tráfico y direcciones sobre el grafo...")

fig, ax = ox.plot_graph(
    G, 
    node_color=colores_nodos_estaticos, 
    node_size=tamaños_nodos_estaticos, 
    edge_color=colores_aristas,          # Lista de colores de tráfico aplicada aquí
    edge_linewidth=grosores_aristas,     # Grosores dinámicos por importancia/congestión
    bgcolor='#1A252F',                   # Fondo más oscuro para que resalten los colores vivos
    show=True,
    close=False
)

fig.savefig("grafo_trafico_bogota.png", dpi=300, bbox_inches='tight')
print("¡Grafo guardado como 'grafo_trafico_bogota.png'!")
