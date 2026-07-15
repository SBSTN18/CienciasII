# controllers/graph_controller.py

import osmnx as ox
from shapely.geometry import Polygon
from models.graph_model import GraphModel
from algorithms.traffic import Traffic


class GraphController:

    def __init__(self):
        self._model = GraphModel()
        self._traffic = Traffic()

    def build_graph(self, vertices):
        polygon = Polygon(vertices)
        graph = ox.graph_from_polygon(polygon, network_type="drive")
        self._traffic.assign_traffic(graph)

        self._model.set_graph(graph)
        self._model.set_polygon_vertices(vertices)
        self._model.set_nodes(list(graph.nodes()))
        self._model.set_edges(ox.graph_to_gdfs(graph, nodes=False, edges=True))

        return self._model

    def resolve_node(self, graph, longitude, latitude):
        return ox.nearest_nodes(graph, X=longitude, Y=latitude)

    def assign_nodes_to_locations(self, locations, graph):
        for location in locations:
            node = self.resolve_node(
                graph,
                location.get_longitude(),
                location.get_latitude()
            )
            location.set_node(node)

    def get_model(self):
        return self._model
    
    def assign_random_nodes(self, locations, graph, occupied_nodes):
        import random
        available = [n for n in graph.nodes() if n not in occupied_nodes]
        for location in locations:
            node = random.choice(available)
            location.set_node(node)
            occupied_nodes.append(node)