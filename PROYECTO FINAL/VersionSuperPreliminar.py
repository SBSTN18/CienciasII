#pip install scikit-learn
#pip install osmnx networkx shapely matplotlib folium scipy
import random
import osmnx as ox
from shapely.geometry import Polygon

# Coordenadas (lon, lat) enviadas para tu polígono en Bogotá
vertices = [
    (-74.04386349649933, 4.73687829082848),
    (-74.0447295756188, 4.733479655821377),
    (-74.04530696406282, 4.730674425272359),
    (-74.04570397994186, 4.7284986211566755),
    (-74.0458122339449, 4.727653442320009),
    (-74.04599266022808, 4.726430632798214),
    (-74.04613699884533, 4.725099930578622),
    (-74.04629938488983, 4.724020982949434),
    (-74.04646177433187, 4.723283701241106),
    (-74.0465880753214, 4.722330629552364),
    (-74.04660611887061, 4.7222766819297135),
    (-74.04682264141381, 4.721575362435682),
    (-74.04685872581621, 4.720820096362574),
    (-74.0470932916398, 4.719291574254637),
    (-74.04736397867026, 4.7180328052297975),
    (-74.04740006265975, 4.717565256365627),
    (-74.04628133685608, 4.717187634289544),
    (-74.04503631072008, 4.716756064336578),
    (-74.04377324660396, 4.716090718831783),
    (-74.0436469409019, 4.716262650077385),
    (-74.04296128064311, 4.716036776285384),
    (-74.04112082847341, 4.715425375302112),
    (-74.0390999438294, 4.714526243726781),
    (-74.03736775541176, 4.7140047376945935),
    (-74.03630317864385, 4.713699022256317),
    (-74.03522055503579, 4.713375319873493),
    (-74.03325157300465, 4.71287153251547),
    (-74.03245984358647, 4.712440162458109),
    (-74.03240570496858, 4.715425307113998),
    (-74.03226133523916, 4.717817002068702),
    (-74.03220717183218, 4.7203885216867185),
    (-74.03215298887463, 4.723283739043789),
    (-74.03213488155895, 4.726035119360662),
    (-74.0320808976595, 4.7284985670175494),
    (-74.03197259328455, 4.731393734962747),
    (-74.03624899999163, 4.733371815807979),
    (-74.0384142652035, 4.734360853242749),
    (-74.0386653627064, 4.734684538731766), 
    (-74.03942472473348, 4.734882346741331),
    (-74.03951494448631, 4.735152085252191),
    (-74.04007430725898, 4.735116120124343),
    (-74.04220345640205, 4.7363568246499685),
    (-74.04290716533959, 4.736554628843316),
    (-74.04386348946643, 4.7368063794497886)
]

poligono = Polygon(vertices)

G = ox.graph_from_polygon(poligono, network_type="drive")

# Asigna 40 km/h por defecto a las calles urbanas que no tengan velocidad seteada
G = ox.add_edge_speeds(G, fallback=40)

# Calcula el atributo 'travel_time' (tiempo de viaje en segundos) para cada arista
G = ox.add_edge_travel_times(G)

coordenadas_manuales = {
    "estacion":       (-74.04428521600079, 4.724541307144894),
    "hospital1":       (-74.04653875922429, 4.72008969821011),
    "hospital2":       (-74.03778628785662, 4.730907698448973),
    "emergencia_1":   (-74.0350, 4.7150),
    "emergencia_2":   (-74.0440, 4.7320),
    "emergencia_3":   (-74.0330, 4.7250),
    "emergencia_4":   (-74.0400, 4.7220),
    "emergencia_5":   (-74.04097798593078, 4.72938661307904),
    "emergencia_6": (-74.03707573765102, 4.731824847566808)
}

# Buscamos de manera automática el nodo real del mapa más cercano a cada coordenada elegida
nodo_estacion     = ox.nearest_nodes(G, X=coordenadas_manuales["estacion"][0],     Y=coordenadas_manuales["estacion"][1])
nodo_hospital_1     = ox.nearest_nodes(G, X=coordenadas_manuales["hospital1"][0],     Y=coordenadas_manuales["hospital1"][1])
nodo_hospital_2     = ox.nearest_nodes(G, X=coordenadas_manuales["hospital2"][0],     Y=coordenadas_manuales["hospital2"][1])
nodo_emergencia_1 = ox.nearest_nodes(G, X=coordenadas_manuales["emergencia_1"][0], Y=coordenadas_manuales["emergencia_1"][1])
nodo_emergencia_2 = ox.nearest_nodes(G, X=coordenadas_manuales["emergencia_2"][0], Y=coordenadas_manuales["emergencia_2"][1])
nodo_emergencia_3 = ox.nearest_nodes(G, X=coordenadas_manuales["emergencia_3"][0], Y=coordenadas_manuales["emergencia_3"][1])
nodo_emergencia_4 = ox.nearest_nodes(G, X=coordenadas_manuales["emergencia_4"][0], Y=coordenadas_manuales["emergencia_4"][1])
nodo_emergencia_5 = ox.nearest_nodes(G, X=coordenadas_manuales["emergencia_5"][0], Y=coordenadas_manuales["emergencia_5"][1])
nodo_emergencia_6 = ox.nearest_nodes(G, X=coordenadas_manuales["emergencia_6"][0], Y=coordenadas_manuales["emergencia_6"][1])

# Mapeamos los IDs de los nodos con sus respectivos nombres y roles
pois = {
    nodo_estacion:     {"nombre": "Estación de Policia", "tipo": "estacion"},
    nodo_hospital_1:     {"nombre": "Hospital A", "tipo": "hospital"},
    nodo_hospital_2:     {"nombre": "Hospital B", "tipo": "hospital"},
    nodo_emergencia_1: {"nombre": "Punto Crítico: Zona de Comercio", "tipo": "emergencia"},
    nodo_emergencia_2: {"nombre": "Punto Crítico: Colegio", "tipo": "emergencia"},
    nodo_emergencia_3: {"nombre": "Punto Crítico: Residencial A", "tipo": "emergencia"},
    nodo_emergencia_4: {"nombre": "Punto Crítico: Residencial B", "tipo": "emergencia"},
    nodo_emergencia_5: {"nombre": "Punto Crítico: Intersección Vial", "tipo": "emergencia"},
    nodo_emergencia_6: {"nombre": "Punto Crítico: Residencial C", "tipo": "emergencia"},
    
}

# Guardamos las etiquetas de manera oficial dentro de las propiedades del grafo G
for nodo_id, informacion in pois.items():
    G.nodes[nodo_id]['nombre'] = informacion['nombre']
    G.nodes[nodo_id]['tipo'] = informacion['tipo']

# Dar estilo a los nodos
colores_nodos = []
tamaños_nodos = []

for nodo in G.nodes():
    if nodo in pois:
        if pois[nodo]['tipo'] == 'estacion':
            colores_nodos.append('#78E911')       # Rojo para la Estación
            tamaños_nodos.append(90)
        elif pois[nodo]['tipo'] == 'hospital':
            colores_nodos.append('#005EB8')      # Azul para el Hospital
            tamaños_nodos.append(90)
        else:
            colores_nodos.append("#FF0000")    # Naranja para las Emergencias
            tamaños_nodos.append(70)
    else:
        colores_nodos.append('#BDC3C7')       # Gris claro para calles/esquinas normales
        tamaños_nodos.append(15)

# Visualizacion
print("Generando mapa base con puntos críticos personalizados...")
fig, ax = ox.plot_graph(
    G, 
    node_color=colores_nodos, 
    node_size=tamaños_nodos, 
    edge_color='#E5E7E9', 
    edge_linewidth=1.0,
    bgcolor='#2C3E50'
)