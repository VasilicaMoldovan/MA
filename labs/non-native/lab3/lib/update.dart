import 'package:flutter/material.dart';
import 'package:lab3/repository/FloralLabRepository.dart';
import 'package:lab3/model/Flower.dart';
import 'package:lab3/listall.dart';

class Update extends StatelessWidget
{
  final FloralLabRepository floralLabRepository = FloralLabRepository();

  Flower flower;
  Update(this.flower);
  TextEditingController inputController = new TextEditingController();

  String dropdownValue = 'Thread';

  var nameInput;
  var colorInput;
  var numberInput;
  var priceInput;
  var typeInput = null;

  void setInitialValues() {
    nameInput = flower.name;
    colorInput = flower.color;
    typeInput = flower.type;
    numberInput = flower.number.toString();
    priceInput = flower.price.toString();
    dropdownValue = flower.type;
  }

  @override
  Widget build(BuildContext context) {
    setInitialValues();
    return MaterialApp(
        title: 'Floral Lab',
        home: Builder( builder: (context) {
          return new Scaffold(
            appBar: AppBar(
              title: Text('Floral Lab'),
            ),
            body: Container(
              constraints: BoxConstraints.expand(),
              decoration: BoxDecoration(
                  image: DecorationImage(
                      image: AssetImage("assets/white.jpg"),
                      fit: BoxFit.cover)),
              child: new Column(mainAxisAlignment: MainAxisAlignment.center,

                  children: <Widget>[
                    new TextField(
                      decoration: InputDecoration(
                        border: OutlineInputBorder(
                          borderSide: BorderSide(color: Colors.black, width: 5.0),
                        ),
                        hintText: flower.name,
                        contentPadding: const EdgeInsets.all(30.0),
                      ),
                      style: TextStyle(fontSize: 25),
                      onChanged: (name) {
                        nameInput = name;
                      } ,
                    ),
                    new TextField(
                        decoration: InputDecoration(
                          border: OutlineInputBorder(
                            borderSide: BorderSide(color: Colors.black, width: 5.0),
                          ),
                          hintText:  flower.color,
                          contentPadding: const EdgeInsets.all(30.0),
                        ),
                        style: TextStyle(fontSize: 25),
                        onChanged: (color) {
                          colorInput = color;
                        }
                    ),
                    new TextField(
                        decoration: InputDecoration(
                          border: OutlineInputBorder(
                            borderSide: BorderSide(color: Colors.black, width: 5.0),
                          ),
                          hintText:  flower.number.toString(),
                          contentPadding: const EdgeInsets.all(30.0),
                        ),
                        style: TextStyle(fontSize: 25),
                        onChanged: (number) {
                          numberInput = number;
                        }
                    ),
                    new TextField(
                        decoration: InputDecoration(
                          border: OutlineInputBorder(
                            borderSide: BorderSide(color: Colors.black, width: 5.0),
                          ),
                          hintText:  flower.price.toString(),
                          contentPadding: const EdgeInsets.all(30.0),
                        ),
                        style: TextStyle(fontSize: 25),
                        onChanged: (price) {
                          priceInput = price;
                        }
                    ),
                    new DropdownButton<String>(
                      value: dropdownValue,
                      icon: Icon(Icons.arrow_downward),
                      iconSize: 24,
                      elevation: 16,
                      style: TextStyle(color: Colors.deepPurple),
                      underline: Container(
                        height: 2,
                        color: Colors.deepPurpleAccent,
                      ),
                      onChanged: (String newValue) {
                        dropdownValue = newValue;
                        typeInput = newValue;
                      },
                      items: <String>['Thread', 'Flowerpot']
                          .map<DropdownMenuItem<String>>((String value) {
                        return DropdownMenuItem<String>(
                          value: value,
                          child: Text(value),
                        );
                      }).toList(),
                    ),
                    new FlatButton(
                      child: new Text("Update", style: new TextStyle(fontSize: 26)),
                      textColor: Colors.black,
                      height: 140.0,
                      splashColor:
                      Colors.amberAccent,
                      onPressed: () {
                        floralLabRepository.updateFlower(flower.getId(), nameInput, typeInput, colorInput, int.parse(numberInput), double.parse(priceInput));
                        Navigator.push(context,
                          new MaterialPageRoute(builder: (context) => MyListPage(title: 'Floral Lab')),
                        );},
                    ),
                  ]
              ),
            ),
          );
        }));
  }
}