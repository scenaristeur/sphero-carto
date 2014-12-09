class Point {
  float x;
  float y;
  float velocityX=0;
  float velocityY=0;
  String couleurS="";
  // color couleur=new color();

  Point(float _x, float _y) {
    x=_x;
    y=_y;
  }
  Point(float _x, float _y, float _velocityX, float _velocityY, String _couleurS) {
    x=_x;
    y=_y;
    velocityX=_velocityX;
    velocityY=_velocityY;
    couleurS=_couleurS;
    println(couleurS);
    println(x);
  }
  void draw() {
    //  fill(155);
    //text(couleurS,100,) ;
    if (couleurS.equals("rouge")) {
      //println("rouge");
      fill(255, 0, 0);
      //  stroke(255-velocityX-velocityY, velocityX, velocityY);
      ellipse(x, y, 8, 8);
    } else if (couleurS.equals("bleu")) {
      //pour retrouver Sphero !!!
      fill(0, 0, 255);
      ellipse(x, y, 10, 10);
    } else {
      noFill();
      stroke(velocityX, 255-velocityX-velocityY, velocityY);
      // text(couleurS,100,-100);
      // println(,-100,-100);

      //fill(255,0,0);
      //  noFill();
      ellipse(x, y, 3, 3);
    }
  }
  void ligne(Point p) {
    line(x, y, p.x, p.y);
  }
  float getX(){
    return this.x;
  }
  float getY(){
    return this.y;
  }
}
