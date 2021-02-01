class Flower
{
   int _id;
   String _name;
   String _type;
   String _color;
   int _number;
   double _price;
   static int _idx = 0;

   Flower(this._name, this._type, this._color, this._number, this._price){
     _id = _idx;
     _idx++;
   }

   @override
  String toString() {
    return 'Flower{name: $_name, type: $_type, color: $_color, number: $_number, price: $_price}';
  }

  int getId() {
     return _id;
  }

   String get name => _name;

  set price(double value) {
    _price = value;
  }

  set number(int value) {
    _number = value;
  }

  set color(String value) {
    _color = value;
  }

  set type(String value) {
    _type = value;
  }

  set name(String value) {
    _name = value;
  }

   String get type => _type;

   double get price => _price;

  int get number => _number;

  String get color => _color;
}