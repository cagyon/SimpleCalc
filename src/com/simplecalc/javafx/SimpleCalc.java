package com.simplecalc.javafx;
	
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class SimpleCalc extends Application {
	
	private final static String[] numberButtons = {"7","8","9","4","5","6","1","2","3","","0",""};
	private final static String[] opButtons     = {"+", "-", "*", "/", "="};
	
	private TextField 	display;        	// l'affichage de la calculatrice
    private String    	lastOp  = "=";      // dernier opérateur saisi
    private boolean   	newNumber = true;  	// le caractère est-il le premier chiffre d'un nouveau nombre?
    private Calculator 	calc;				// le calculateur proprement dit
	
    public void reset() {
    	// Remettre la calculatrice dans l'état initial
    	newNumber = true;
        display.setText("0");
        lastOp = "=";
        calc.setTotal("0");
    }
    
	@Override
	public void start(Stage primaryStage) {
		try {
			// Donner un titre à la fenêtre et la rendre non-redimensionnable
			primaryStage.setTitle("Simple Calc");	
			primaryStage.setResizable(false);
			
			// Créer la Scene de l'application et y placer un BorderPane
			BorderPane root = new BorderPane();
			Scene scene = new Scene(root,300,450);
			//scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			
			// Création du calculateur
			calc = new Calculator();
			
			// ***
	        // AFFICHAGE DE LA CALCULATRICE
	        // **
	    	
	    	// Créer l'affichage de la calculatrice : champ texte aligné à droite
	    	display = new TextField("0");
	    	display.setAlignment(Pos.CENTER_RIGHT);
	    	display.setFont(Font.font("Verdana", FontWeight.NORMAL, 20));
	    	// Le placer en haut du BorderPane
	    	root.setTop(display);

	    	// ***
	        // BOUTONS 0...9 
	        // **
	    	
	        // Créer un GridPane pour y placer les boutons chiffres
	    	GridPane numbersPane = new GridPane();
	    	// Le placer au centre du BorderPane et le centrer
	    	root.setCenter(numbersPane);
	    	numbersPane.setAlignment(Pos.CENTER);
	    	numbersPane.setPadding(new Insets(25, 25, 25, 25));
	    	// Espacer les eléments contenus dans le GridPane
	    	numbersPane.setHgap(10);
	    	numbersPane.setVgap(10);
	    	
	    	// Créer un handler sous forme de classe anonyme;
	    	EventHandler<MouseEvent> numberHandler = new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					// Récupérer le nom du bouton qui a été cliqué
					String chiffre = ((Node) event.getSource()).getId();
					
		        	// Si c'est le premier chiffre d'un nouveau nombre, l'afficher 
					// et attendre soit un autre chiffre, soit un opérateur
			        if (newNumber) {
			        	display.setText(chiffre);
			        	newNumber = false;
			        } else {
			            // Sinon, l'ajouter à l'affichage existant
			        	display.setText(display.getText() + chiffre);
			        }
				}
	    	};
	    	
	        // Placer les boutons de haut en bas: 789, 456, 123, 0
	    	int row = 0, column = 0;
	    	
	    	for (int i = 0; i < numberButtons.length; i++) {
	            Button b = new Button(numberButtons[i]);
		    	b.setFont(Font.font("Verdana", FontWeight.NORMAL, 30));
		    	// Nommer chaque bouton
		    	b.setId(numberButtons[i]);
	            // Ajouter le bouton au Pane
	            if (!numberButtons[i].isEmpty()) {
	            	numbersPane.add(b, column, row);
	            }
	            column++;
	            if (column%3 == 0) {
	            	column = 0;
	            	row++;
	            }
	         // Ajouter le handler à chaque bouton		 
		    	b.setOnMouseClicked(numberHandler);
	        }
			
	    	// ***
	        // BOUTONS OPERATEURS
	        // **
	        
	        // Créer un GridPane et y placer les boutons opérateurs
	    	GridPane opPane = new GridPane();
	    	// Le placer à droite du BorderPane et le centrer
	    	root.setRight(opPane);
	    	opPane.setAlignment(Pos.CENTER);
	    	opPane.setHgap(10);
	    	opPane.setVgap(10);
	    	opPane.setPadding(new Insets(25, 25, 25, 25));
	    	
	    	// Créer un handler sous forme de classe anonyme
	    	EventHandler<MouseEvent> opHandler = new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					// Le prochain bouton sera le premier chiffre d'un nouveau nombre
			           newNumber = true;
			           try {
			            	// Lire la valeur courante et faire l'opération
			                String affichage = display.getText();
			                
			                if (lastOp.equals("=")) {
			                	calc.setTotal(affichage);
			                } 
			                else if (lastOp.equals("+")) {
			                	calc.add(affichage);
			                } 
			                else if (lastOp.equals("-")) {
			                	calc.subtract(affichage);
			                } 
			                else if (lastOp.equals("*")) {
			                	calc.multiply(affichage);
			                } 
			                else if (lastOp.equals("/")) {
			                		calc.divide(affichage);
			                }
			                // Afficher le résultat
			                int total = calc.getTotalString();
			                if (total > 10) {
				                display.setText("Erreur");
			                }
			                else {
			                	display.setText(Integer.toString(total));
			                	// int -> String : Integer.toString()
			                	// String -> int : Integer.parseInt()
			                }
			                
			            } 
			           catch (NumberFormatException | ArithmeticException ex) { // nouveauté Java 7
			            	// NumberFormatException : un caractère non-numérique a été saisi
			            	// ArithmeticException : une division par zéro a été tentée
			                reset();
			                display.setText("Erreur");
			            }
			            // Mémoriser le dernier opérateur
			            lastOp = ((Node) event.getSource()).getId();
				}
	    	};
	    	
	        // Placer les boutons de haut en bas: +, -, *, /, =
	        for (int i = 0; i < opButtons.length; i++) {
	            Button b = new Button(opButtons[i]);
		    	b.setFont(Font.font("Verdana", FontWeight.NORMAL, 20));
		    	b.setId(opButtons[i]);
	            // Ajouter le bouton au panneau
	            opPane.add(b, 0, row);
	            row++;
	        	// Ajouter un handler à chaque bouton
		    	b.setOnMouseClicked(opHandler);
	        }

	        // ***
	        // BOUTON EFFACER
	        // **
	        
	        // Créer un GridPane et y placer le bouton effacer
	    	GridPane clearPane = new GridPane();
	    	clearPane.setAlignment(Pos.TOP_CENTER);
	    	clearPane.setHgap(10);
	    	clearPane.setVgap(10);
	    	clearPane.setPadding(new Insets(25, 25, 25, 25));
	    	Button b = new Button("Effacer");
	    	b.setFont(Font.font("Verdana", FontWeight.NORMAL, 20));
	    	 // Ajouter le bouton au panneau
	    	clearPane.add(b, 0, 0);
	    	root.setBottom(clearPane);
	    	// Ajouter le handler au chaque bouton : classe anonyme dérivant de EventHandler
	    	b.setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					reset();
				}
	    	});

			primaryStage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
