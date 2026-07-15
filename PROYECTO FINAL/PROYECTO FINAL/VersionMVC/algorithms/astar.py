import math
import networkx as nx


class AStar:

    def __init__(self):
        pass

    def heuristic(self, node1, node2, graph):
        """
        Distancia Haversine entre dos nodos del grafo.
        """

        lat1 = graph.nodes[node1]["y"]
        lon1 = graph.nodes[node1]["x"]

        lat2 = graph.nodes[node2]["y"]
        lon2 = graph.nodes[node2]["x"]

        R = 6371000

        phi1 = math.radians(lat1)
        phi2 = math.radians(lat2)

        delta_phi = math.radians(lat2 - lat1)
        delta_lambda = math.radians(lon2 - lon1)

        a = (
            math.sin(delta_phi / 2) ** 2
            + math.cos(phi1)
            * math.cos(phi2)
            * math.sin(delta_lambda / 2) ** 2
        )

        c = 2 * math.atan2(math.sqrt(a), math.sqrt(1 - a))

        return R * c

    def calculate_route(self, graph, origin, destination):
        """
        Calcula la ruta óptima utilizando A*
        """

        route = nx.astar_path(
            graph,
            origin,
            destination,
            heuristic=lambda n1, n2: self.heuristic(
                n1,
                n2,
                graph
            ),
            weight="travel_time"
        )

        total_time = nx.path_weight(
            graph,
            route,
            "travel_time"
        )

        total_distance = nx.path_weight(
            graph,
            route,
            "length"
        )

        return route, total_time, total_distance