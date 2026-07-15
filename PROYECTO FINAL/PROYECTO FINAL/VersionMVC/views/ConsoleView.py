class ConsoleView:

    def show_menu(self):
        print("\n" + "=" * 60)
        print("      SISTEMA DE RESPUESTA A EMERGENCIAS")
        print("=" * 60)
        print("\nSeleccione una opcion:")
        print("1. Emergencia aleatoria")
        print("2. Elegir emergencia manualmente")

    def show_emergency_list(self, emergencies):
        print("\nEmergencias disponibles:\n")
        for i, emergency in enumerate(emergencies, 1):
            print(f"{i}. {emergency.get_name()}")

    def ask_option(self):
        return input("\nOpcion: ").strip()

    def ask_emergency_selection(self):
        return input("\nSeleccione una emergencia: ").strip()

    def show_station_analysis(self, stations):
        print("\n" + "=" * 60)
        print("          ANALISIS DE ESTACIONES")
        print("=" * 60)
        for station in stations:
            if station.get_response_time() > 0:
                print(f"\n{station.get_name()}")
                print(f"Tiempo: {station.get_response_time() / 60:.2f} minutos")
                print(f"Distancia: {station.get_distance() / 1000:.2f} km")

    def show_result(self, emergency, station):
        print("\n" + "=" * 60)
        print("        RESULTADO DE LA EMERGENCIA")
        print("=" * 60)
        print(f"\nEmergencia: {emergency.get_name()}")
        print(f"Estacion seleccionada: {station.get_name()}")
        print(f"Tiempo estimado: {station.get_response_time() / 60:.2f} minutos")
        print(f"Distancia: {station.get_distance() / 1000:.2f} km")
        print(f"Nodos recorridos: {len(station.get_route())}")

    def show_comparison(self, comparison):
        print("\n" + "=" * 60)
        print("   COMPARACION: RUTAS A* INDIVIDUALES vs RED MST")
        print("=" * 60)

        print("\n--- RUTAS INDIVIDUALES (A*) ---")
        for r in comparison["best_routes"]:
            print(f"\n  {r['emergency']}")
            print(f"    -> {r['station']}")
            print(f"    Tiempo: {r['time']/60:.2f} min | "
                  f"Distancia: {r['distance']/1000:.2f} km")

        ind_time = comparison["individual_total_time"]
        ind_dist = comparison["individual_total_distance"]
        mst_time = comparison["mst_total_time"]
        mst_dist = comparison["mst_total_distance"]

        print(f"\n  COSTO TOTAL INDIVIDUAL: {ind_time/60:.2f} min | "
              f"{ind_dist/1000:.2f} km")

        print("\n--- RED DE PATRULLAJE (MST - Kruskal) ---")
        print(f"\n  Nodos conectados: {comparison['num_pois']}")
        print(f"  Aristas del MST: {len(comparison['mst_edges'])}")
        print(f"  COSTO TOTAL MST: {mst_time/60:.2f} min | "
              f"{mst_dist/1000:.2f} km")

        print("\n--- METRICAS DE COMPARACION ---")
        print(f"  Ratio de cobertura (individual/MST): "
              f"{comparison['coverage_ratio']:.2f}x")
        print(f"  Estaciones: {comparison['num_stations']}")
        print(f"  Puntos criticos: {comparison['num_emergencies']}")

        print("\n--- RECOMENDACIONES ---")
        if comparison["coverage_ratio"] > 1.5:
            print("  * La red MST es significativamente mas eficiente que")
            print("    las rutas individuales.")
            print("  * Se recomienda implementar el MST como red principal")
            print("    de patrullaje para reducir costos operativos.")
        elif comparison["coverage_ratio"] > 1.0:
            print("  * La red MST ofrece una mejora moderada sobre las")
            print("    rutas individuales.")
            print("  * Utilizar MST para patrullaje preventivo.")
        else:
            print("  * Las rutas individuales A* son competitivas.")
            print("  * El MST es util para cobertura periodica de todos")
            print("    los puntos de interes.")

        print(f"\n  * Tiempo promedio por emergencia (A*): "
              f"{ind_time/comparison['num_emergencies']/60:.2f} min")

        if mst_time > 0:
            print(f"  * Tiempo para recorrer toda la red MST: "
                  f"{mst_time/60:.2f} min")

        print("=" * 60)

    def show_vehicle_status(self, ambulances, patrols):
        print("\n" + "=" * 60)
        print("         ESTADO DE VEHICULOS")
        print("=" * 60)
        print("\n--- AMBULANCIAS ---")
        for a in ambulances:
            status = "EN RUTA" if not a.is_available() else "DISPONIBLE"
            route_len = len(a.get_current_route()) if a.get_current_route() else 0
            print(f"  {a.get_code()}: {status} (nodos ruta: {route_len})")

        print("\n--- PATRULLAS (Red MST) ---")
        for p in patrols:
            status = "PATRULLANDO" if not p.is_available() else "EN BASE"
            route_len = len(p.get_current_route()) if p.get_current_route() else 0
            print(f"  {p.get_code()}: {status} (nodos ruta: {route_len})")

    def show_error(self, message):
        print(f"\n[ERROR] {message}")

    def show_message(self, message):
        print(f"\n{message}")
