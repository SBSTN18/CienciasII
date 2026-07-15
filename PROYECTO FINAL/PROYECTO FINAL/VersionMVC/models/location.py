class Location:

    def __init__(self, name, latitude, longitude):
        self._name = name
        self._latitude = latitude
        self._longitude = longitude
        self._node = None

    def get_name(self):
        return self._name

    def get_latitude(self):
        return self._latitude

    def get_longitude(self):
        return self._longitude

    def get_node(self):
        return self._node

    def set_node(self, node):
        self._node = node