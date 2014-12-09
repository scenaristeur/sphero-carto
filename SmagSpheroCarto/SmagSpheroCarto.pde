BufferedReader reader;
String line;
ArrayList<Point> points=new ArrayList<Point>();
int nb=0;
Point lastPoint=new Point(.53537, .7383);

void setup() {
  //size(800, 600);
  // Open the file from the createWriter() example
  text("ouverture", 100, 100);
  reader = createReader("/storage/emulated/0/smag-sphero/Smag-Sphero-last.txt");   
  try {
    nb++;
    text(nb, 100, 110);
    line = reader.readLine();
    text(line, 100, 120);
  } 
  catch (IOException e) {
    e.printStackTrace();
    text("erreur", 100, 100);
    line = null;
  }
}

void draw() {
  background(0);

  fill(155);
  translate(width/2, height/2);
  for (int i=0; i<points.size (); i++) {
    Point point=(Point)points.get(i );
    point.draw();
    if (i>0) {
      point.ligne(points.get(i-1));
    }
  }
  if (line != null) {
    //regler la vitesse de lecture nombre de points rajoutés a chaque passage
    for (int j=0; j<10; j++) {
      readLigne();
      ligneSuivante();
    }
  } else {
    noLoop();
  }

  // noLoop();
  // saveFrame("sphero-carto.png");
} 
void readLigne() {

  if (line != null) {
  //text("creation des points", 100, 100);
  // println(line);
  String[] pieces = split(line, ",");
  if (pieces[1]!=null) {
    float x = float(pieces[0])/2;
    float y = float(pieces[1])/2;
    Point point;
    if (pieces.length>2) {
      float velX=float(pieces[2]);
      float velY=float(pieces[3]);
      String couleur=pieces[4];
      point=new Point(x, -y, velX, velY, couleur);
    } else {
      point= new Point(x, -y);
    }

    //println(point.x);
   //supprimer les points en double
    if ((point.getX()==lastPoint.getX())&&(point.getY()==lastPoint.getY())) {
      points.remove(lastPoint);
      points.add(point);
    }else{
      points.add(point);
    }
    lastPoint=point;
    text(points.size(), 100, 100);
  }}
}
void ligneSuivante() {
  
  if (line != null) {
  try {
    nb++;
    text(nb, 100, 110);
    line = reader.readLine();
  } 
  catch (IOException e) {
    e.printStackTrace();
    line = null;
  }}
}
