import 'package:lab3/model/Flower.dart';
import 'dart:async';

class FloralLabRepository
{
  List<Flower> flowerList;
  static final FloralLabRepository _floralLabRepository = FloralLabRepository._internal();

  factory FloralLabRepository() {
    return _floralLabRepository;
  }

  FloralLabRepository._internal() {
    flowerList = <Flower>[];
    flowerList.add(new Flower("Tulip", "Thread", "White", 10, 11));
    flowerList.add(new Flower("Rose", "Thread", "Red", 20, 10));
    flowerList.add(new Flower("Peonies", "Flowerpot", "Pink", 200, 13));
    flowerList.add(new Flower("Daisy", "Flowerpot", "White", 200, 11));
    flowerList.add(new Flower("Lily", "Thread", "White", 279, 7));
    flowerList.add(new Flower("Iris", "Thread", "Purple", 223, 9));
  }

  List<Flower> getAllFlowers() {

    return flowerList;
  }

  Future<List<String>> getAllFlowerNames() async {
    List<String> flowerNames = <String>[];

    for (int i = 0; i < flowerList.length; i++) {
      flowerNames.add(flowerList[i].name);
    }

    return flowerNames;
  }

  static FloralLabRepository getInstance() {
    if (_floralLabRepository == null)
        return new FloralLabRepository();
    return _floralLabRepository;
  }

  void addFlower(String name, String type, String color, int number, double price) {
    flowerList.add(new Flower(name, type, color, number, price));
  }

  void deleteFlower(Flower flower) {
    flowerList.remove(flower);

    print(flowerList.toString());
  }

  Flower updateFlower(int id, String name, String type, String color, int number, double price) {
    for(int i = 0; i < flowerList.length; i++) {
      if (flowerList[i].getId() == id) {
        flowerList[i].name = name;
        flowerList[i].type = type;
        flowerList[i].color = color;
        flowerList[i].number = number;
        flowerList[i].price = price;

        return flowerList[i];
      }
    }
    return null;
  }

}