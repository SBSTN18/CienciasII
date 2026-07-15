# algorithms/traffic.py

class Traffic:

    def __init__(self):
        pass

    def assign_traffic(self, graph):
        for u, v, k, data in graph.edges(keys=True, data=True):
            speed = self._speed_by_road_type(data.get('highway', 'residential'))
            data['speed_kph'] = speed
            data['travel_time'] = data['length'] / (speed / 3.6)

    def _speed_by_road_type(self, road_type):
        if isinstance(road_type, list):
            road_type = road_type[0]

        if road_type in ['primary', 'trunk']:
            return 50.0 * 0.30    # ~15 km/h  rojo
        elif road_type in ['secondary', 'tertiary']:
            return 50.0 * 0.50    # ~25 km/h  amarillo
        else:
            return 30.0 * 0.90    # ~27 km/h  verde