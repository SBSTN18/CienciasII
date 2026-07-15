import random
from config.settings import (
    VERTICES, STATIONS_DATA, HOSPITALS_DATA, EMERGENCIES_DATA
)
from controllers.GraphController import GraphController
from controllers.EmergencyController import EmergencyController
from views.ConsoleView import ConsoleView
from views.MapView import MapView
from models.station import Station
from models.hospital import Hospital
from models.emergency import Emergency
from models.ambulance import Ambulance
from models.patrol import Patrol


class AppController:

    def __init__(self):
        self._console = ConsoleView()
        self._graph_controller = GraphController()
        self._stations = []
        self._hospitals = []
        self._emergencies = []
        self._ambulances = []
        self._patrols = []
        self._graph_model = None
        self._emergency_controller = None
        self._map_view = None

    def run(self):
        self._setup()

        emergency = self._select_emergency()
        best_station, route = self._emergency_controller.find_best_station(
            self._stations, emergency
        )

        ambulance = self._dispatch_ambulance(best_station, emergency, route)

        self._show_results(emergency, best_station, ambulance)

        all_poi_nodes = self._collect_all_poi_nodes()
        comparison = self._emergency_controller.run_comparison(
            self._stations, self._emergencies, all_poi_nodes
        )
        mst_edges, mst_time, _ = self._emergency_controller.compute_mst(
            all_poi_nodes, weight="travel_time"
        )

        self._assign_patrol_routes(mst_edges)

        self._console.show_comparison(comparison)
        self._console.show_vehicle_status(self._ambulances, self._patrols)

        self._generate_maps(emergency, best_station, route, mst_edges, comparison)

    def _setup(self):
        self._console.show_message("Cargando mapa de Bogota...")
        self._build_locations()
        self._build_vehicles()

        self._graph_model = self._graph_controller.build_graph(VERTICES)
        graph = self._graph_model.get_graph()

        self._graph_controller.assign_nodes_to_locations(self._stations, graph)
        self._graph_controller.assign_nodes_to_locations(self._hospitals, graph)

        occupied = [loc.get_node() for loc in self._stations + self._hospitals]
        self._graph_controller.assign_random_nodes(self._emergencies, graph, occupied)

        occupied += [v.get_current_node() for v in self._ambulances + self._patrols
                     if v.get_current_node() is not None]

        self._emergency_controller = EmergencyController(graph)
        self._map_view = MapView(graph, self._graph_model.get_edges())

    def _build_locations(self):
        for data in STATIONS_DATA:
            self._stations.append(Station(data["name"], data["lat"], data["lon"]))

        for data in HOSPITALS_DATA:
            self._hospitals.append(Hospital(data["name"], data["lat"], data["lon"]))

        for data in EMERGENCIES_DATA:
            self._emergencies.append(Emergency(data["name"], 0, 0))

    def _build_vehicles(self):
        self._ambulances = [
            Ambulance("AMB-001"),
            Ambulance("AMB-002"),
        ]
        self._patrols = [
            Patrol("PATROL-101"),
            Patrol("PATROL-102"),
        ]

    def _collect_all_poi_nodes(self):
        nodes = []
        for loc in self._stations + self._hospitals + self._emergencies:
            n = loc.get_node()
            if n is not None:
                nodes.append(n)
        return nodes

    def _dispatch_ambulance(self, station, emergency, route):
        available = [a for a in self._ambulances if a.is_available()]
        if available:
            ambulance = available[0]
            ambulance.set_current_node(station.get_node())
            ambulance.set_current_route(route)
            ambulance.set_available(False)
            return ambulance
        return None

    def _assign_patrol_routes(self, mst_edges):
        for i, patrol in enumerate(self._patrols):
            if i < len(mst_edges):
                u, v, _ = mst_edges[i]
                patrol.set_current_node(u)
                patrol.set_current_route([u, v])
                patrol.set_available(False)

    def _select_emergency(self):
        while True:
            self._console.show_menu()
            option = self._console.ask_option()

            if option == "1":
                return self._emergency_controller.select_emergency(
                    self._emergencies, "random"
                )
            elif option == "2":
                return self._select_manual_emergency()
            else:
                self._console.show_error("Opcion invalida.")

    def _select_manual_emergency(self):
        while True:
            self._console.show_emergency_list(self._emergencies)
            try:
                selection = int(self._console.ask_emergency_selection()) - 1
                if 0 <= selection < len(self._emergencies):
                    return self._emergency_controller.select_emergency(
                        self._emergencies, selection
                    )
                self._console.show_error("Seleccion fuera de rango.")
            except ValueError:
                self._console.show_error("Ingrese un numero valido.")

    def _show_results(self, emergency, best_station, ambulance):
        self._console.show_station_analysis(self._stations)
        self._console.show_result(emergency, best_station)
        if ambulance:
            self._console.show_message(
                f"Ambulancia {ambulance.get_code()} despachada "
                f"desde {best_station.get_name()}"
            )

    def _generate_maps(self, emergency, best_station, route, mst_edges, comparison):
        all_locations = (self._stations + self._hospitals +
                         self._emergencies + self._ambulances + self._patrols)
        interactive_map = self._map_view.build_interactive_map(
            all_locations, best_station, emergency, route, mst_edges
        )
        self._map_view.save_interactive_map(interactive_map)
        self._map_view.save_static_map(all_locations)
