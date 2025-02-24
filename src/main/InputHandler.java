package main;

import java.awt.event.*;

public class InputHandler implements KeyListener, MouseListener, MouseMotionListener {

    // Keyboard input
    public boolean upPressed, downPressed, leftPressed, rightPressed;

    // Mouse input
    public int mouseX, mouseY;
    public boolean leftClick;

    // --- KeyListener methods ---
    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_W) {
            upPressed = true;
        }
        if (code == KeyEvent.VK_S) {
            downPressed = true;
        }
        if (code == KeyEvent.VK_A) {
            leftPressed = true;
        }
        if (code == KeyEvent.VK_D) {
            rightPressed = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_W) {
            upPressed = false;
        }
        if (code == KeyEvent.VK_S) {
            downPressed = false;
        }
        if (code == KeyEvent.VK_A) {
            leftPressed = false;
        }
        if (code == KeyEvent.VK_D) {
            rightPressed = false;
        }
    }

    // --- MouseListener methods ---
    @Override
    public void mouseClicked(MouseEvent e) {
        // Not used, but you could also process clicks here.
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // Detect left mouse button press.
        if (e.getButton() == MouseEvent.BUTTON1) {
            leftClick = true;
            // Update the mouse position when clicked.
            mouseX = e.getX();
            mouseY = e.getY();
            System.out.println("Left click at: (" + mouseX + ", " + mouseY + ")");
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            leftClick = false;
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // Optional: Handle mouse entering the component.
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // Optional: Handle mouse exiting the component.
    }

    // --- MouseMotionListener methods ---
    @Override
    public void mouseDragged(MouseEvent e) {
        // Update the current mouse coordinates while dragging.
        mouseX = e.getX();
        mouseY = e.getY();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        // Update the current mouse coordinates.
        mouseX = e.getX();
        mouseY = e.getY();
    }
}
