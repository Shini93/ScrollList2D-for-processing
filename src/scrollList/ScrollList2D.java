package scrollList;

import java.util.ArrayList;
import processing.core.*;

/**
 * This is a template class and can be used to start a new processing Library.
 * Make sure you rename this class as well as the name of the example package 'template' 
 * to your own Library naming convention.
 * 
 * (the tag example followed by the name of an example included in folder 'examples' will
 * automatically include the example in the javadoc.)
 *
 * @example Hello 
 */

public class ScrollList2D extends PApplet {
	// myParent is a reference to the parent sketch
	PApplet myParent;
	PVector size;
	  PVector pos;
	  int hFieldX, hFieldY;      //size of each field
	  int count = 0;
	  int maxX, maxY;
	  ArrayList <ArrayList <String>> fields = new ArrayList <ArrayList <String>>();
	  ArrayList <String> dummyListRow  = new ArrayList <String>();
	  int bg = color(200,200,200);
	  int border = 0;
	  private float percY = 0;
	  private float percX = 0;
	  int strokeW = 0;
	  int strokeC = 0;
	  boolean lockedGlobal = false;
	  int sLocked = -1;
	  PGraphics g;      //can draw on any given PGraphics object
	
	  Slider sliderV;
	  Slider sliderH;
	int myVariable = 0;
	
	public final static String VERSION = "##library.prettyVersion##";
	
	
	public ScrollList2D(PApplet theParent, int px, int py, int sx, int sy, int nrX, int nrY) {
		myParent = theParent;
	    //g = createGraphics(width,height);      
	    g = theParent.getGraphics();                  //gets standard draw object
	    maxX = nrX;
	    maxY = nrY;
	    size = new PVector(sx, sy);
	    pos = new PVector(px, py);
	    hFieldX = round((float)sx / (float)nrX);
	    hFieldY = round((float)sy / (float)nrY);
	    setSlider();
	  }
	
	public void setStrokeWeight(int w) {
		strokeW = w;
	}
	
	public static String version() {
		return VERSION;
	}

	public void setGraphicContext(PGraphics g){
	    this.g = g; 
	    sliderV.setGraphicContext(g);
	    sliderH.setGraphicContext(g);
	  }
	
	 public void setFieldSize(int x, int y) {
		    hFieldX = round((size.x) / (float)x);
		    hFieldY = round((size.y) / (float)y);
		    //setSlider();
		  }

	  void setSlider() {
	    int px = (int)pos.x;
	    int py = (int)pos.y;
	    int sy = (int)size.y;
	    int sx = (int)size.x;

	   
	    sliderH = new Slider(myParent,px, py + sy, sx, 10, 10, false);
	    sliderV = new Slider(myParent,sx + px, py, sy, 10, 10, true);
	  }

	  public void nextRow() {
	    count ++;
	    fields.add(dummyListRow);
	    dummyListRow  = new ArrayList <String>();
	  }

	  public void update() {
		if(sLocked == 0 && sliderV.lockedGlobal == false) {
			sLocked = -1;
			lockedGlobal = false;
			sliderH.lockedGlobal = false;
		}
		else if(sLocked == 1 && sliderH.lockedGlobal == false) {
			sLocked = -1;
			lockedGlobal = false;
			sliderV.lockedGlobal = false;
		}
		sliderV.display();
		sliderV.update(10);
		if(sliderV.lockedGlobal == true) {
			lockedGlobal = true;
			sliderH.lockedGlobal = true;
			sLocked = 0;
		}
	    sliderH.display();
	    sliderH.update(10);
	    if(sliderH.lockedGlobal == true) {
			lockedGlobal = true;
			sliderV.lockedGlobal = true;
			sLocked = 1;
		}
	    percY = sliderV.getPosCalc() - 1;
	    percX = sliderH.getPosCalc() - 1;
	  }

	  public void drawList() {
	    drawCanvas();
	    update();
	    drawFields();
	  }
	  
	  public void drawCanvas() {
	    g.stroke(border);
	    g.fill(bg);
	    g.rect(pos.x, pos.y, size.x, size.y);
	  }

	  public void drawField(int y, int r, int c) {
	    if (checkFieldVisible(r, y) == true) {
	      float sy = maxY / (size.y / (float)hFieldY) - 1;
	      float sx = maxX / (size.x / (float)hFieldX) - 1;
	      int posY = round(size.y * percY * sy) + hFieldY;
	      int posX = round(size.x * percX * sx) + hFieldX;
	      int px = round(pos.x + (y - 1) * hFieldX + posX);
	      int py = round(pos.y + ( r - 1 )* hFieldY +  posY);
	      int w = hFieldX;
	      int h = hFieldY;
	      int dy = py;
	      int dx = px;
	      int tW = round(g.textWidth(fields.get(y).get(r)));
	      int tH = round(g.textAscent() + round(g.textDescent() ));
	      
	      if(px + w > pos.x + size.x)
	        w = round(pos.x + size.x - px);
	      if(px < pos.x)
	        px = round(pos.x);
	      if(py + h > pos.y + size.y)
	        h = round(pos.y + size.y - py);
	      if(py < pos.y) {
	        py = round(pos.y);
	      }
	      	      
	      g.fill(c);
	      if (strokeW > 0) {
	        g.strokeWeight(strokeW);
	        g.stroke(strokeC);
	        g.rect(px, py, w, h);
	      }
	      g.fill(0);
	      
	      if(py != dy) {
	    	  py -= pos.y - dy;
	      }
	      if(px != dx) {
	    	  px -= pos.x - dx;
	      }
	      
	      
	      int tPosX = round((float) (0.5 * (hFieldX - tW)));
	      if(px + tPosX + tW > size.x + pos.x)
	        return;
	      if(dy < pos.y - h + 2*tH)
	        return;
	      if(py + 0.5*h + tH > pos.y + size.y)
	        return;
	      if(dx < pos.x - 0.5 *w + tW)
	    	  return;
	      g.text(fields.get(y).get(r), px + tPosX, py + (float)0.5*h +tH);
	    }
	  }
	
	  public void drawFields() {
	    g.fill(0);
	    for (int y = 0; y < fields.size(); y ++) {
	      for (int r = 0; r < fields.get(y).size(); r++) {
	        drawField(y, r, 200);
	      }
	    }
	    g.fill(125);
	  }
	
	  public void addField(String text) {
	    dummyListRow.add(text);
	    if (dummyListRow.size() >= maxY)
	      nextRow();
	  }
	
	  boolean checkFieldVisible(int nr, int r) {
	    float sy = maxY / (size.y / (float)hFieldY) - 1;
	    float sx = maxX / (size.x / (float)hFieldX) - 1;
	    float bottom = (size.y / (hFieldY)) * (1 - sy*percY);
	    float right = (size.x / hFieldX) * (1 - sx*percX);
	    if (nr + 0 > bottom)    //bottom blend out
	      return false;
	    if (nr + 1  < bottom - (size.y / (hFieldY)))    //bottom blend out
	      return false;
	    if (r + 0 > right)    //bottom blend out
	      return false;
	    if (r + 1  < right - (size.x / hFieldX) )    //bottom blend out
	      return false;
	    return true;
	  }
	
	  public int[] itemClicked() {
	    int[] id = {-1, -1};
	    if (myParent.mouseX < pos.x || myParent.mouseX > pos.x + size.x)
	      return id;
	    if (myParent.mouseY < pos.y || myParent.mouseY > pos.y + size.y)
	      return id;
	
	    int offsetY = hFieldY;
	    int offsetX = hFieldX;
	    float sy = maxY / (size.y / (float)hFieldY) - 1;
	    float sx = maxX / (size.x / (float)hFieldX) - 1;
	    int posY = round(size.y * percY * sy + pos.y) + offsetY ;
	    int posX = round(size.x * percX * sx + pos.x) + offsetX ;
	
	    for (int y = 0; y < fields.size(); y ++) {
	      for (int r = 0; r < fields.get(y).size(); r++) {
	        if (checkFieldVisible(r, y) == true) {
	          if (myParent.mouseY > posY + (r - 1) * hFieldY && myParent.mouseY < posY + (r ) * hFieldY) {
	            id[0] = r;
	            if (id[1] >= fields.size())
	              id[0] = -1;
	          }
	          if (myParent.mouseX > posX + (y - 1 ) * hFieldX && myParent.mouseX < posX + (y ) * hFieldX) {
	            id[1] = y;
	            if (id[0] >= fields.get(y).size())
	              id[1] = -1;
	          }
	        }
	      }
	    }
	    return id;
	  }
}

class Slider extends PApplet {
	private int swidth, sheight;    // width and height of bar
	private  float xpos, ypos;       // x and y position of bar
	private   float spos, newspos;    // x position of slider
	private   float sposMin, sposMax; // max and min values of slider
	private   float loose;              // how loose/heavy
	private   boolean over;           // is the mouse over the slider?
	private   boolean locked = false;
	private   float ratio;
	private   String name = "";
	private   PVector range = new PVector(0, 1);
	int sColor = color(255,0,0);
	int bColor = color(100,100,40);
	int hColor = color(0,230,0);
	boolean vert = false;
	public boolean lockedGlobal = false;
	PGraphics g;
	PApplet parent;

	  Slider (PApplet theParent, float xp, float yp, int sw, int sh, int l, boolean vertical) {
		  parent = theParent;
	    init(xp, yp, sw, sh, l, vertical);
	  }

	  void setGraphicContext(PGraphics g){
	    this.g = g; 
	    
	  }

	  void setColor(int[] c) {
	    sColor = c[0];
	    bColor = c[1];
	    hColor = c[2];
	    int b = color(0);
	  }

	  void setSliderPos(float perc) {  //in percent
	    float range = swidth/2 - sheight/2;
	    spos = xpos + 2 * perc * range;
	    newspos = spos;
	  }

	  void init(float xp, float yp, int sw, int sh, int l, boolean vertical) {
	    vert = vertical;
	    if (vert == true) {
	      int widthtoheight = sh - sw;
	      ratio = (float)sh / (float)widthtoheight;
	      swidth = sh;
	      sheight = sw;
	      xpos = xp-swidth/2;
	      ypos = yp;
	      spos = ypos;
	      sposMin = ypos;
	      sposMax = ypos + sheight - swidth;
	    } else {
	      int widthtoheight = sw - sh;
	      ratio = (float)sw / (float)widthtoheight;
	      swidth = sw;
	      sheight = sh;
	      xpos = xp;
	      ypos = yp-sheight/2;
	      spos = xpos;
	      sposMin = xpos;
	      sposMax = xpos + swidth - sheight;
	    }
	    newspos = spos;
	    loose = l;
	    g = parent.getGraphics();
	  }

	  Slider(String name, float xp, float yp, int sw, int sh, int l, boolean vertical) {
	    this.name = name;
	    init(xp, yp, sw, sh, l, vertical);
	  }

	  void defRange(float min, float max) {
	    range = new PVector(min, max);
	  }

	  void update(int textWidth) {
	    if (overEvent()) {
	      over = true;
	    } else {
	      over = false;
	    }
	    if (parent.mousePressed && over && lockedGlobal == false) {
	      locked = true;
	      lockedGlobal = true;
	    }
	    if (!parent.mousePressed) {
	      locked = false;
	      lockedGlobal = false;
	    }
	    if (locked) {
	      if (vert == true)
	        newspos = constrain(parent.mouseY, sposMin, sposMax );
	      else
	        newspos = constrain(parent.mouseX-sheight/2 - textWidth, sposMin, sposMax);
	    }
	    if (abs(newspos - spos) > 1) {
	      float nLoose = min(loose, abs(newspos - spos));
	      spos = spos + (newspos-spos)/nLoose;
	    }
	  }

	  float constrainSl(float val, float minv, float maxv) {
	    return min(max(val, minv), maxv);
	  }

	  boolean overEvent() {
	    if (parent.mouseX > xpos && parent.mouseX < xpos+swidth &&
	    		parent.mouseY > ypos && parent.mouseY < ypos+sheight) {
	      return true;
	    } else {
	      return false;
	    }
	  }

	  void display() {
	    g.noStroke();
	    g.fill(bColor);
	    g.rect(xpos, ypos, swidth, sheight);
	    if (over || locked) {
	      g.fill(sColor);
	    } else {
	      g.fill(color(hColor));
	    }
	    if (vert == true)
	      g.rect(xpos, spos, swidth, swidth);
	    else
	      g.rect(spos, ypos, sheight, sheight);
	  }

	  float getPos() {
	    // Convert spos to be values between
	    // 0 and the total width of the scrollbar
	    return spos * ratio;
	  }

	  float getPosCalc() {
	    int roundSl;
	    if (vert == true)
	      roundSl = round(100 * ((((spos - sheight) - ypos + swidth) * ratio) / swidth));
	    else
	      //round = round(100 * (((spos ) * ratio) / swidth)+ sheight);
	      roundSl = round(100 * (1 -((spos - xpos) / (swidth - sheight))));
	    return (0.01f * roundSl) ;
	  }

	  float getRangedOutput() {
	    return map(getPosCalc(), 0, 1, range.x, range.y);
	  }
}