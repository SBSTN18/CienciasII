

class Vehicle:

    def __init__(self, code):
        self._code = code
        self._current_node = None
        self._available = True
        self._current_route = []

    def get_code(self):
        return self._code

    def get_current_node(self):
        return self._current_node

    def set_current_node(self, node):
        self._current_node = node

    def is_available(self):
        return self._available

    def set_available(self, available):
        self._available = available

    def get_current_route(self):
        return self._current_route

    def set_current_route(self, route):
        self._current_route = route

    def get_name(self):
        return f"{self.__class__.__name__} {self._code}"

    def get_node(self):
        return self._current_node