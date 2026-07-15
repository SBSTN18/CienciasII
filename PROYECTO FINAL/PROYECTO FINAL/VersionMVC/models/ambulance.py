from models.vehicle import Vehicle

class Ambulance(Vehicle):

    def __init__(self, code):
        super().__init__(code)