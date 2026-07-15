from algorithms.mst import MST
from algorithms.astar import AStar


class Comparison:

    def compare(self, graph, stations, emergencies, all_poi_nodes):
        """
        Compara el costo de rutas A* individuales vs el MST optimizado.

        Retorna un diccionario con todas las metricas.
        """
        astar = AStar()
        mst_calc = MST()

        individual_time = 0
        individual_distance = 0
        best_routes = []

        for emergency in emergencies:
            best_time = float("inf")
            best_dist = float("inf")
            best_station = None
            best_route = None

            for station in stations:
                try:
                    route, time, dist = astar.calculate_route(
                        graph, station.get_node(), emergency.get_node()
                    )
                    if time < best_time:
                        best_time = time
                        best_dist = dist
                        best_station = station
                        best_route = route
                except Exception:
                    continue

            if best_station is not None:
                individual_time += best_time
                individual_distance += best_dist
                best_routes.append({
                    "emergency": emergency.get_name(),
                    "station": best_station.get_name(),
                    "time": best_time,
                    "distance": best_dist,
                    "route": best_route
                })

        mst_edges, mst_total_time, complete_edges = mst_calc.build_mst_over_pois(
            graph, all_poi_nodes, weight="travel_time"
        )

        mst_edges_dist, mst_total_dist, _ = mst_calc.build_mst_over_pois(
            graph, all_poi_nodes, weight="length"
        )

        coverage_ratio = individual_time / mst_total_time if mst_total_time > 0 else 0

        return {
            "individual_total_time": individual_time,
            "individual_total_distance": individual_distance,
            "mst_total_time": mst_total_time,
            "mst_total_distance": mst_total_dist,
            "mst_edges": mst_edges,
            "mst_edges_dist": mst_edges_dist,
            "best_routes": best_routes,
            "coverage_ratio": coverage_ratio,
            "num_emergencies": len(emergencies),
            "num_stations": len(stations),
            "num_pois": len(all_poi_nodes)
        }
