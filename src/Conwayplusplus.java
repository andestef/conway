import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import java.util.concurrent.CompletableFuture;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.layout.GridPane;
import javafx.geometry.Insets;  
import javafx.geometry.Pos;
import java.util.ArrayList;
import java.io.File;
import java.util.Scanner;
import javafx.scene.input.KeyCode;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.io.FileWriter;
public class Conwayplusplus extends Application {  
    public class button extends Button{
        public boolean black;
        public button(){
            black = false;
            super();
        }
    }
    public class cell{
        public int state;
        public int x;
        public int y;
        public cell(int xv,int yv){
            state = 1;
            x = xv;
            y = yv;
        }
        public cell(int xv,int yv,int st){
            state = st;
            x = xv;
            y = yv;
        }
    }
    ArrayList<ArrayList<button>> cellDisplays;
    ArrayList<ArrayList<cell>> cells;
    int sizeX;
    int sizeY;
    int cellsizeX;
    int cellsizeY;
    Button sim;
    Button oneGen;
    //Label moneys;
    Button placeCells;
    Button placeWalls;
    Button clear;
   public static void main(String[] args) {
        launch(args);
    }
    public void updateCell(int x, int y,int state){
        button c = getBtn(x,y);
        if (state == 0){
            c.setStyle("-fx-background-color: #000000;-fx-border-width: 0.5px; -fx-border-style: solid; -fx-border-color: #000000;");
            c.black = true;
            cells.get(x).get(y).state = 0;
        }
        else if (state == 1){
            c.setStyle("-fx-background-color: #FFFFFF;-fx-border-width: 0.5px; -fx-border-style: solid; -fx-border-color: #000000;");
            c.black = false;
            cells.get(x).get(y).state = 1;
        }
    }
    public button getBtn(int x, int y){
        return cellDisplays.get(x).get(y);
    }
    public void simGen(){
        ArrayList<ArrayList<cell>> newcells = new ArrayList<ArrayList<cell>>();
        for(int x=0;x<sizeX;x++){
            newcells.add(new ArrayList<cell>());
            for(int y=0;y<sizeY;y++){
                int neighbors = 0;
                if(x-1>=0){
                    if(cells.get(x-1).get(y).state == 0){
                        neighbors++;
                    }
                    if(y-1>=0){
                        if(cells.get(x-1).get(y-1).state == 0){
                        neighbors++;
                    }
                    }
                    if(y+1<sizeY){
                        if(cells.get(x-1).get(y+1).state == 0){
                        neighbors++;
                    }
                    }
                }
                if(x+1<sizeX){
                    if(cells.get(x+1).get(y).state == 0){
                        neighbors++;
                    }
                    if(y-1>=0){
                        if(cells.get(x+1).get(y-1).state == 0){
                        neighbors++;
                    }
                    }
                    if(y+1<sizeY){
                        if(cells.get(x+1).get(y+1).state == 0){
                        neighbors++;
                    }
                    }
                }
                if(y-1>=0){
                    if(cells.get(x).get(y-1).state == 0){
                        neighbors++;
                    }
                }
                if(y+1<sizeY){
                    if(cells.get(x).get(y+1).state == 0){
                        neighbors++;
                    }
                }
                if (neighbors < 2 || neighbors > 3){
                    newcells.get(x).add(new cell(x,y,1));
                }
                else if(neighbors == 3 && cells.get(x).get(y).state == 1){
                    newcells.get(x).add(new cell(x,y,0));
                }
                else{
                    newcells.get(x).add(cells.get(x).get(y));
                }
            }
        }
        for(int x=0;x<sizeX;x++){
            for(int y=0;y<sizeY;y++){
                updateCell(x,y,newcells.get(x).get(y).state);
            }
        }
    }
    boolean ppn;
    public void rasync(){
        if (ppn){
        simGen();
        try{
        Thread.sleep(500);
        }
        catch(InterruptedException e){
             
        }
        rasync();
        }
    }
    public void pp(Button b){
        if (ppn){
            b.setText("Play");
            b.setOnAction(event->{
                pp(b);
            });
            ppn = false;
        }
        else{
            ppn = true;
            b.setOnAction(event->{
                pp(b);
            });
            b.setText("Pause");
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                rasync();
            });
        }
    }
    public void makeConwayScreen(Stage stage){
        GridPane root = new GridPane();
        root.setAlignment(Pos.TOP_CENTER);
        cellDisplays = new ArrayList<ArrayList<button>>();
        cells = new ArrayList<ArrayList<cell>>();
        int x = 0;
        int y = 0;
        for(x=0;x<sizeY;x++){
            cellDisplays.add(new ArrayList<button>());
            cells.add(new ArrayList<cell>());
            for(y=0;y<sizeX;y++){
                cell thiscell = new cell(x,y);
                button c = new button();
                c.setText(" ");
                c.setStyle("-fx-background-color: #FFFFFF;-fx-border-width: 0.5px; -fx-border-style: solid; -fx-border-color: #000000;");
                c.black = false;
                c.setPrefSize(cellsizeX,cellsizeY);
                c.setOnAction(event -> {
                    if(c.black){
                        updateCell(thiscell.x,thiscell.y,1);
                    }
                    else{
                        updateCell(thiscell.x,thiscell.y,0);
                    }
                });
                root.add(c,x,y);
                cellDisplays.get(x).add(c);
                cells.get(x).add(thiscell);
            }
        }
        stage.hide();
        ppn = false;
        sim = new Button("Play");
        oneGen = new Button("+1 Generation");
        root.add(sim,x,0);
        root.add(oneGen,x,1);
        oneGen.setOnAction(event -> {
            simGen();
        });
        sim.setOnAction(event->{
            pp(sim);
        });
        stage.setScene(new Scene(root));
        stage.setMaximized(true);
        stage.show();
    }
    public void start(Stage stage) {
        cellsizeX = 20;
        cellsizeY = 20;
        stage.setTitle("Conway");
        GridPane root = new GridPane();
        sizeX = 25;
        sizeY = 25;
        //root.setAlignment(Pos.CENTER); 
        stage.setScene(new Scene(root));
        root.setFocusTraversable(true);
        root.requestFocus();
        stage.setMaximized(true);
        stage.show();
        makeConwayScreen(stage);
    }
}