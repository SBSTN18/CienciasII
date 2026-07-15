import networkx as nx
from algorithms.astar import AStar


class UnionFind:

    def __init__(self, n):
        self._parent = list(range(n))
        self._rank = [0] * n

    def find(self, x):
        if self._parent[x] != x:
            self._parent[x] = self.find(self._parent[x])
        return self._parent[x]

    def union(self, x, y):
        rx, ry = self.find(x), self.find(y)
        if rx == ry:
            return False
        if self._rank[rx] < self._rank[ry]:
            self._parent[rx] = ry
        elif self._rank[rx] > self._rank[ry]:
            self._parent[ry] = rx
        else:
            self._parent[ry] = rx
            self._rank[rx] += 1
        return True


class MST:

    def kruskal(self, nodes, edges_with_weights):
        """
        Implementacion del algoritmo de Kruskal con Union-Find.

        nodes: lista de identificadores de nodos
        edges_with_weights: lista de tuplas (u, v, peso)
        Retorna: lista de aristas del MST [(u, v, peso), ...]
        """
        n = len(nodes)
        if n <= 1:
            return []

        node_to_index = {node: i for i, node in enumerate(nodes)}
        uf = UnionFind(n)

        sorted_edges = sorted(edges_with_weights, key=lambda e: e[2])
        mst_edges = []

        for u, v, weight in sorted_edges:
            iu = node_to_index.get(u)
            iv = node_to_index.get(v)
            if iu is None or iv is None:
                continue
            if uf.union(iu, iv):
                mst_edges.append((u, v, weight))

            if len(mst_edges) == n - 1:
                break

        return mst_edges

    def build_poi_graph(self, graph, poi_nodes, weight="travel_time"):
        """
        Construye un grafo completo entre todos los nodos de interes (POI)
        donde el peso de cada arista es el costo de la ruta mas corta entre ellos.
        """
        astar = AStar()
        n = len(poi_nodes)
        edges = []

        for i in range(n):
            for j in range(i + 1, n):
                try:
                    _, total_time, total_dist = astar.calculate_route(
                        graph, poi_nodes[i], poi_nodes[j]
                    )
                    w = total_dist if weight == "length" else total_time
                    edges.append((poi_nodes[i], poi_nodes[j], w))
                except Exception:
                    edges.append((poi_nodes[i], poi_nodes[j], float("inf")))

        return edges

    def build_mst_over_pois(self, graph, poi_nodes, weight="travel_time"):
        """
        Calcula el MST sobre los nodos de interes usando Kruskal.
        Retorna: (mst_edges, total_cost, poi_graph_edges)
        """
        complete_edges = self.build_poi_graph(graph, poi_nodes, weight)
        mst_edges = self.kruskal(poi_nodes, complete_edges)

        total_cost = sum(e[2] for e in mst_edges)

        return mst_edges, total_cost, complete_edges
