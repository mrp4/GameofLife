package scot.provan.gameoflife;

import android.graphics.Color;

/**
 * Created by mrp4 on 01/12/14.
 */
public class GameOfLifeBoard {
    public class Cell {
        private Cell north;
        private Cell east;
        private Cell south;
        private Cell west;
        private int color;
        private boolean active;

        public Cell getNorth() {
            return north;
        }

        public void setNorth(Cell north) {
            this.north = north;
        }

        public Cell getEast() {
            return east;
        }

        public void setEast(Cell east) {
            this.east = east;
        }

        public Cell getSouth() {
            return south;
        }

        public void setSouth(Cell south) {
            this.south = south;
        }

        public Cell getWest() {
            return west;
        }

        public void setWest(Cell west) {
            this.west = west;
        }

        public int getColor() {
            return color;
        }

        public void setColor(int color) {
            this.color = color;
        }

        public boolean isActive() {
            return active;
        }

        public void performGame() {
            int nAlive = 0;
            if (north != null && north.isActive()) {
                nAlive++;
            }
            if (north != null && north.getEast() != null && north.getEast().isActive()) {
                nAlive++;
            }
            if (east != null && east.isActive()) {
                nAlive++;
            }
            if (east != null && east.getSouth() != null && east.getSouth().isActive()) {
                nAlive++;
            }
            if (south != null && south.isActive()) {
                nAlive++;
            }
            if (south != null && south.getWest() != null && south.getWest().isActive()) {
                nAlive++;
            }
            if (west != null && west.isActive()) {
                nAlive++;
            }
            if (west != null && west.getNorth() != null && west.getNorth().isActive()) {
                nAlive++;
            }
            if (active) {
                if (nAlive < 2) {
                    active = false;
                } else if (nAlive < 4) {
                    active = true;
                } else {
                    active = false;
                }
            } else {
                if (nAlive == 3) {
                    active = true;
                }
            }

        }

        public void activateCell() {
            active = true;
        }
    }

    private Cell[][] cells;

    public Cell[][] getCells() {
        return cells;
    }

    public Cell getCell(int x, int y) throws CellOutOfBoundsException {
        if (y < 0 || y >= cells.length || x < 0 || x >= cells[y].length) {
            throw new CellOutOfBoundsException();
        }
        return cells[y][x];
    }

    public class CellOutOfBoundsException extends Exception {

    }

    public GameOfLifeBoard(int numberX, int numberY) {
        cells = new Cell[numberY][numberX];
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[i].length; j++) {
                cells[i][j] = new Cell();
            }
        }
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[i].length; j++) {
                Cell c = cells[i][j];
                Cell north = (i != 0) ? cells[i - 1][j] : null;
                Cell east = (j < cells[i].length - 1) ? cells[i][j + 1] : null;
                Cell south = (i < cells.length - 1) ? cells[i + 1][j] : null;
                Cell west = (j != 0) ? cells[i][j - 1] : null;
                c.setNorth(north);
                c.setEast(east);
                c.setSouth(south);
                c.setWest(west);
                int color = Color.argb(255, 255, 0, 0);
                c.setColor(color);
            }
        }
    }

    public void performGame() {
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[i].length; j++) {
                Cell c = cells[i][j];
                c.performGame();
            }
        }
    }
}
