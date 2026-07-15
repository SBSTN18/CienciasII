from models.location import Location


class Hospital(Location):

    def __init__(self, name, latitude, longitude):
        super().__init__(name, latitude, longitude)