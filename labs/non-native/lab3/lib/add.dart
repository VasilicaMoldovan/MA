import 'package:flutter/material.dart';
import 'package:lab3/repository/FloralLabRepository.dart';
import 'package:lab3/listall.dart';

class Add extends StatelessWidget
{
  TextEditingController inputController = new TextEditingController();

  final FloralLabRepository floralLabRepository = FloralLabRepository();

  String dropdownValue = 'Thread';

  var nameInput;
  var colorInput;
  var numberInput;
  var priceInput;
  var typeInput;

  @override
  Widget build(BuildContext context) {
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
                child: new Column(
                    mainAxisAlignment: MainAxisAlignment.center,

                    children: <Widget>[
                      new TextField(
                        decoration: InputDecoration(
                          border:  OutlineInputBorder(
                            borderSide: BorderSide(color: Colors.black, width: 5.0),
                          ),
                          hintText: 'Name',
                          contentPadding: const EdgeInsets.all(30.0),
                        ),
                        style: TextStyle(fontSize: 25),
                        onChanged: (name) {
                          nameInput = name;
                        } ,
                      ),
                      new TextField(
                          decoration: InputDecoration(
                            border:  OutlineInputBorder(
                              borderSide: BorderSide(color: Colors.black, width: 5.0),
                            ),
                            hintText:  'Color',
                            contentPadding: const EdgeInsets.all(30.0),
                          ),
                          style: TextStyle(fontSize: 25),
                          onChanged: (color) {
                            colorInput = color;
                          }
                      ),
                      new TextField(
                          decoration: InputDecoration(
                            border:  OutlineInputBorder(
                              borderSide: BorderSide(color: Colors.black, width: 5.0),
                            ),
                            hintText:  'Number',
                            contentPadding: const EdgeInsets.all(30.0),
                          ),
                          style: TextStyle(fontSize: 25),
                          onChanged: (number) {
                            numberInput = number;
                          }
                      ),
                      new TextField(
                          decoration: InputDecoration(
                            border:  OutlineInputBorder(
                              borderSide: BorderSide(color: Colors.black, width: 5.0),
                            ),
                            hintText:  'Price',
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
                      new TextButton(
                        child: new Text("Add", style: new TextStyle(fontSize: 26)),
                        style: TextButton.styleFrom(
                          primary: Colors.amberAccent,
                        ),
                        onPressed: () {
                          print("Clicked");
                          print(nameInput);
                          print(typeInput);
                          floralLabRepository.addFlower(nameInput, typeInput, colorInput, int.parse(numberInput), double.parse(priceInput));
                          Navigator.push(context,
                            new MaterialPageRoute(builder: (context) => MyListPage(title: 'Floral Lab')),
                          );},
                      ),
                      new TextButton(
                        child: new Text("Go Back", style: new TextStyle(fontSize: 26)),
                        style: TextButton.styleFrom(
                          primary: Colors.amberAccent,
                        ),
                        onPressed: () {
                          print("Clicked");
                          Navigator.push(context,
                            new MaterialPageRoute(builder: (context) => MyListPage(title: 'Floral Lab')),
                          );},
                      ),
                    ]
                ),
              )
          );
        }));
  }

}