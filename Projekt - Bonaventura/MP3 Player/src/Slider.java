import java.awt.Component;
import java.awt.Container;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;
import javax.swing.Timer;



public class Slider extends JPanel{
    
public  enum  direct{
        Left,
        Right,
        up,
        Dowun
    };
   
  public Slider() {
        setBorder(javax.swing.BorderFactory.createEtchedBorder());         
    }

   public void nextSlidPanel(Component ShowPanel,direct DirectionMove) {  
       nextSlidPanel(10, 40, ShowPanel, DirectionMove);      
   }       
   
   public void nextSlidPanel(int SpeedPanel, Component ShowPanel,direct DirectionMove) { 
       nextSlidPanel(SpeedPanel, 40, ShowPanel, DirectionMove);        
   }      
   
   
   public void nextSlidPanel(int SpeedPanel, int TimeSpeed,Component ShowPanel, direct DirectionMove) { 
       if (!ShowPanel.getName().equals(getCurrentComponentShow(this))) { 
           Component currentComp = getCurrentComponent(this);           
           ShowPanel.setVisible(true);                     
           JPanelSlidingListener sl = new JPanelSlidingListener(SpeedPanel, currentComp, ShowPanel, DirectionMove);
           Timer t = new Timer(TimeSpeed, sl);            
           sl.timer = t;           
           t.start();           
       }   
       refresh();
   }
 
   
    public void previous(direct direct) {
        Component currentComp=getCurrentComponent(this);
        Component previousComp=getPreviousComponent(this);
        previousComp.setVisible(true);
        JPanelSlidingListener sl=new JPanelSlidingListener(10, currentComp, previousComp, direct);
        Timer t=new Timer(40,sl);
        sl.timer=t;
        t.start();
        refresh();
    }
 
    public void next(direct direct) {
        Component currentComp=getCurrentComponent(this);
        Component nextComp=getNextComponent(this);
        nextComp.setVisible(true);
        JPanelSlidingListener sl=new JPanelSlidingListener(10, currentComp, nextComp, direct);
        Timer t=new Timer(40,sl);
        sl.timer=t;
        t.start();
        refresh();
    }
 
   
  public Component getCurrentComponent(Container parent) {
        Component comp = null;
        int n = parent.getComponentCount();
        for (int i = 0 ; i < n ; i++) {
             comp = parent.getComponent(i);
            if (comp.isVisible()) {
                return comp;
            }
        }
        return comp;
    }
  
  public Component getNextComponent(Container parent) {
        int n = parent.getComponentCount();
        for (int i = 0 ; i < n ; i++) {
            Component comp = parent.getComponent(i);
            if (comp.isVisible()) {
                int currentCard = (i + 1) % n;
                comp = parent.getComponent(currentCard);
                return comp;
            }
        }

        return null;
    }
  public Component getPreviousComponent(Container parent) {
       int  n = parent.getComponentCount();
        for (int i = 0 ; i < n ; i++) {
            Component comp = parent.getComponent(i);
           
            if (comp.isVisible()) {
                int currentCard = ((i > 0) ? i-1 : n-1);
                comp = parent.getComponent(currentCard);
                return comp;
            }

        }
        return null;
    }
 
  public String getCurrentComponentShow(Container parent) {
        String PanelName = null;
        Component comp = null;
        int n = parent.getComponentCount();
        for (int i = 0 ; i < n ; i++) {
             comp = parent.getComponent(i);
            if (comp.isVisible()) {
               PanelName=comp.getName();
                return PanelName;
            }
        }
        return PanelName;
    }
         
  public class JPanelSlidingListener implements ActionListener {
       

        Component schovPanel;
        Component ukazPanel;
        int posuny;
        int posun=0;
        Timer timer;
        direct direct;
 
        public  JPanelSlidingListener(int steps,Component HidePanel, Component ShowPanel,direct direct) {
            this.posuny=steps;
            this.schovPanel=HidePanel;
            this.ukazPanel=ShowPanel;
            this.direct=direct;
        }
 
        @Override
        public void actionPerformed(ActionEvent e) {
           
            Rectangle bounds=schovPanel.getBounds();
            int shift=bounds.width/posuny;
            int shiftup=bounds.height/posuny;
            switch(direct){
                case Left:
                    schovPanel.setLocation(bounds.x-shift, bounds.y);
                    ukazPanel.setLocation(bounds.x-shift+bounds.width, bounds.y);
                    break;
                case Right:
                    schovPanel.setLocation(bounds.x+shift, bounds.y);
                    ukazPanel.setLocation(bounds.x+shift-bounds.width, bounds.y);
                    break;
                    
              case up:
                    schovPanel.setLocation(bounds.x, bounds.y-shiftup);
                    ukazPanel.setLocation(bounds.x, bounds.y-shiftup+bounds.height);
                    break;
                case Dowun:
                     schovPanel.setLocation(bounds.x, bounds.y+shiftup);
                     ukazPanel.setLocation(bounds.x, bounds.y+shiftup-bounds.height);
                    break; 
               
             
            }
            
            
            repaint();     
            posun++;
            
            if (posun==posuny) {
                timer.stop();
                schovPanel.setVisible(false);
                   
                          }
           
            }
        
    }
  
 
      
      public void refresh() {
        revalidate();
        repaint();
    } 
}