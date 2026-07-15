# models/graph_model.py

class GraphModel:

    def __init__(self):
        self._graph = None
        self._nodes = None
        self._edges = None
        self._polygon_vertices = []

    def get_graph(self):
        return self._graph

    def set_graph(self, graph):
        self._graph = graph

    def get_nodes(self):
        return self._nodes

    def set_nodes(self, nodes):
        self._nodes = nodes

    def get_edges(self):
        return self._edges

    def set_edges(self, edges):
        self._edges = edges

    def get_polygon_vertices(self):
        return self._polygon_vertices

    def set_polygon_vertices(self, vertices):
        self._polygon_vertices = vertices