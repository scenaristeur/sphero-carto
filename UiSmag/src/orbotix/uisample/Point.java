package orbotix.uisample;

import android.R.color;



public class Point {
	private float positionX;
	private float positionY;
	private float velocityX;
	private float velocityY;
	private String couleur;
    private float accelX;
    private float accelY;
    private float powerX;
    private float powerY;
	
	Point(){

	}
	Point(float _positionX,float _positionY,float _velocityX,float _velocityY,String _couleur){
		positionX=_positionX;
		positionY=_positionY;
		velocityX=_velocityX;
		velocityY=_velocityY;
		couleur = _couleur;
	}
	Point(float _positionX,float _positionY,float _velocityX,float _velocityY,String _couleur,float _accelX,float _accelY,float _powerX,float _powerY){
		positionX=_positionX;
		positionY=_positionY;
		velocityX=_velocityX;
		velocityY=_velocityY;
		couleur = _couleur;
		accelX=_accelX;
		accelY=_accelY;
		powerX=_powerX;
		powerY=_powerY;
	}
	float getX(){
		return this.positionX;
	}
	float getY(){
		return this.positionY;
	}
	float getVelX(){
		return this.velocityX;
	}
	float getVelY(){
		return this.velocityY;
	}
	String getCouleur(){
	return this.couleur;	
	}
	float getAccelX(){
		return this.accelX;
	}
	float getAccelY(){
		return this.accelY;
	}
	float getPowerX(){
		return this.powerX;
	}
	float getPowerY(){
		return this.powerY;
		
	}
	
}
