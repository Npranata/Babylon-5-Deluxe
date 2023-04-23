package button;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.border.LineBorder;

import edu.macalester.graphics.CanvasWindow;
import edu.macalester.graphics.FontStyle;
import edu.macalester.graphics.GraphicsText;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class NewButton extends ComponentSwing {

    private JButton button;

    public NewButton(String title) {
        this(new JButton(title));
    }

    private NewButton(JButton button) {
        super(button);
        this.button = button;
        button.setFocusable(false);

        // Some Windows versions of Java miscompute the button dimensions so the text
        // gets truncated, plus default Swing style on Windows is ugly.
        // We thus do manual styling + manual size computation. The style below brings
        // appearance and size roughly into parity across Mac and Windows.
        button.setBorder(new LineBorder(Color.red));
        button.setBackground(Color.ORANGE);
        button.setOpaque(true);

        String font = "Tahoma";
        int fontSize = 50;
        // GraphicsText measure = new GraphicsText(button.getText());
        // measure.setFont(font, FontStyle.PLAIN, fontSize);
        button.setFont(new Font(font, Font.PLAIN, fontSize));

        changed(300, 100);

        button.addMouseListener(new MouseAdapter(){

            @Override
            public void mousePressed(MouseEvent e) {
                button.setBackground(Color.red);
            }
        
            @Override
            public void mouseReleased(MouseEvent e) {
                button.setBackground(Color.white);
            }
        
        });
    }

    public void onClick(Runnable callback) {
        button.addActionListener(e -> {
            if (getCanvas() == null) {
                return;
            }

            getCanvas().performEventAction(callback);
        });
    }


    public static void main(String[] args) {
        CanvasWindow window = new CanvasWindow("Yes", 500, 800);
        NewButton button = new NewButton("This");
        window.add(button, 30, 100);

        button.onClick(() -> {
            System.out.println("test2");
        });
    }
}

