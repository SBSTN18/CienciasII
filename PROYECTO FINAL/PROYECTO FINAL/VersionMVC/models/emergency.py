from models.location import Location


class Emergency(Location):

    def __init__(self, name, latitude, longitude):
        super().__init__(name, latitude, longitude)

        self._priority = "MEDIA"

    def get_priority(self):
        return self._priority

    def set_priority(self, priority):
        self._priority = priority