import 'package:flutter/material.dart';
import 'package:lab3/repository/FloralLabRepository.dart';
import 'package:lab3/model/Flower.dart';
import 'package:lab3/update.dart';
import 'package:lab3/add.dart';

class MyListPage extends StatefulWidget {
  MyListPage({Key key, this.title}) : super(key: key);

  final String title;

  @override
  _MyListPageState createState() => _MyListPageState();
}

class _MyListPageState extends State<MyListPage> {
  final FloralLabRepository floralLabRepository = FloralLabRepository();
  var flowers = FloralLabRepository().getAllFlowers();

  showDeleteDialog(BuildContext context, Flower flower)
  {
    Widget noButton = FlatButton(
        onPressed: (){
          Navigator.of(context, rootNavigator: true).pop('dialog');
        },
        child: Text("No"));
    Widget yesButton = FlatButton(
        onPressed:(){
          print(flower.toString());
          print("lalal");
          floralLabRepository.deleteFlower(flower);
          Navigator.of(context, rootNavigator: true).pop('dialog');
          Navigator.of(context, rootNavigator: true).pop('dialog');
          setState(() {});
        },
        child: Text("Yes"));
    AlertDialog alert = AlertDialog(
      title: Text(""),
      content: Text("Are you sure you want to delete this flower?"),
      actions: [
        noButton,
        yesButton,
      ],
    );
    showDialog(
      context: context,
      builder: (BuildContext context) {
        return alert;
      },
    );
  }

  showAlertDialog(BuildContext context,int index) {
    Widget deleteButton = FlatButton(
      child: Text("Delete"),
      onPressed:  () {
        showDeleteDialog(context, flowers.elementAt(index));
      },
    );
    Widget updateButton = FlatButton(
      child: Text("Update"),
      onPressed:  () {
        Navigator.push(context,
          new MaterialPageRoute(builder: (context) => Update(flowers[index])),
        );
        Navigator.of(context, rootNavigator: true).pop('dialog');
      },
    );

    AlertDialog alert = AlertDialog(
      title: Text("Choose and option"),
      content: Text(""),
      actions: [
        deleteButton,
        updateButton,
      ],
    );
    // show the dialog
    showDialog(
      context: context,
      builder: (BuildContext context) {
        return alert;
      },
    );
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
        title: widget.title,
        theme: ThemeData(
          textTheme: TextTheme(bodyText2: TextStyle(color: Colors.purple, fontSize: 30)),
        ),
        home: Builder( builder: (context) {
          return new Scaffold(
            appBar: AppBar(
              title: Text('Floral Lab'),
            ),
            body: new Container(
                decoration: BoxDecoration(
                    image: DecorationImage(
                        image: AssetImage("assets/white.jpg"),
                        fit: BoxFit.cover)),
                child : FutureBuilder<List<String>> (
                  future: floralLabRepository.getAllFlowerNames(),
                  builder: (context,snapshot)
                  {
                    var elements=snapshot.data;
                    if(elements==null)
                    {
                      return Center(child: CircularProgressIndicator());
                    }
                    return ListView.builder(
                      itemCount: elements.length,
                      itemBuilder: (context,index)
                      {
                        return ListTile(leading: Icon(Icons.star), title: Text(elements[index].toString()),
                            trailing: FlatButton(
                              child: Text('Edit'),
                              onPressed: (){
                                showAlertDialog(context,index);
                              },
                            )
                        );
                      },

                    );
                  },
                )),
            floatingActionButton: FloatingActionButton(
              onPressed: () {
                Navigator.push(context,
                  new MaterialPageRoute(builder: (context) => Add()),
                );
              },
              child: Icon(Icons.add),
              backgroundColor: Colors.orangeAccent,
            ),
          );
        }));
  }
}
