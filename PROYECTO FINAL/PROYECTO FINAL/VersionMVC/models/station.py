from models.location import Location


class Station(Location):

    def __init__(self, name, latitude, longitude):
        super().__init__(name, latitude, longitude)

        self._response_time = 0
        self._distance = 0
        self._route = []

    def get_response_time(self):
        return self._response_time

    def set_response_time(self, response_time):
        self._response_time = response_time

    def get_distance(self):
        return self._distance

    def set_distance(self, distance):
        self._distance = distance

    def get_route(self):
        return self._route

    def set_route(self, route):
        self._route = route