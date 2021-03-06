//  NSGAII_main.java
//
//  Author:
//       Antonio J. Nebro <antonio@lcc.uma.es>
//       Juan J. Durillo <durillo@lcc.uma.es>
//
//  Copyright (c) 2011 Antonio J. Nebro, Juan J. Durillo
//
//  This program is free software: you can redistribute it and/or modify
//  it under the terms of the GNU Lesser General Public License as published by
//  the Free Software Foundation, either version 3 of the License, or
//  (at your option) any later version.
//
//  This program is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//  GNU Lesser General Public License for more details.
// 
//  You should have received a copy of the GNU Lesser General Public License
//  along with this program.  If not, see <http://www.gnu.org/licenses/>.

package p7_nsgaii_main;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

import jmetal.core.Algorithm;
import jmetal.core.Operator;
import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.core.SolutionSet;
import jmetal.metaheuristics.nsgaII.NSGAII;
import jmetal.operators.crossover.CrossoverFactory;
import jmetal.operators.mutation.MutationFactory;
import jmetal.operators.selection.BinaryTournament;
import jmetal.operators.selection.SelectionFactory;
import jmetal.qualityIndicator.QualityIndicator;
import jmetal.util.Configuration;
import jmetal.util.JMException;
import p7_FuncionesTest.ZDT1;
import p7_FuncionesTest.ZDT2;
import p7_FuncionesTest.ZDT3;
import p7_FuncionesTest.ZDT4;
import p7_FuncionesTest.ZDT5;
import p7_FuncionesTest.ZDT6;

/** 
 * Modificaci�n de la clase NSGAII_main.java original.
 * 
 * Class to configure and execute the NSGA-II algorithm.
 * 
 */ 

public class Prac_7_NSGAII_main {
  public static Logger      logger_ ;      // Logger object
  public static FileHandler fileHandler_ ; // FileHandler object
  
  //DEFINICI�N DE CONSTANTES, SEG�N PAG. 182 DE PAPER "Zitzler, Deb and Thiele"
  private static Object POPULATION_SIZE = 100;
  private static Object MAX_EVALUATIONS = 25000;
  
  private static Object CROSSOVER_RATE = 0.8;
  private static Object MUTATION_RATE = 0.01;

  /**
   * @param args Command line arguments.
   * @throws JMException 
   * @throws IOException 
   * @throws SecurityException 
   */
  public static void main(String [] args) throws 
                                  JMException, 
                                  SecurityException, 
                                  IOException, 
                                  ClassNotFoundException {
    Problem   problem = null   ; // The problem to solve
    Algorithm algorithm ; // The algorithm to use
    Operator  crossover ; // Crossover operator
    Operator  mutation  ; // Mutation operator
    Operator  selection ; // Selection operator
    
    HashMap  parameters ; // Operator parameters
    
    QualityIndicator indicators ; // Object to get quality indicators
    
    String solutionType; //"Real", "BinaryReal, "Binary" and "ArrayReal"
    int numberOfVariables; //n�mero de variables de decisi�n

    // Logger object and file to store log messages
//    logger_      = Configuration.logger_ ;
//    fileHandler_ = new FileHandler("NSGAII_main.log"); 
//    logger_.addHandler(fileHandler_) ;
    
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
    String date = sdf.format(new Date());
    String paretoFoundPathFile = null;
    String paretoTruePathFile = null;
    
    FileOutputStream fos   = new FileOutputStream("Informe_Ejecucion_"+date+".txt");
    OutputStreamWriter osw = new OutputStreamWriter(fos)    ;
    BufferedWriter bw      = new BufferedWriter(osw)        ;
    
    for(int i=1;i<=6;i++){
    	System.out.println("\n");
    	switch (i) {
		case 1:
			System.out.println("ZDT 1\n");
		    solutionType = "ArrayReal";
		    numberOfVariables = 30;
		    problem = new ZDT1(solutionType, numberOfVariables);
		    
			break;
		case 2:
			System.out.println("ZDT 2\n");
			solutionType = "ArrayReal";
		    numberOfVariables = 30;
		    problem = new ZDT2(solutionType, numberOfVariables);
		    
			break;
		case 3:
			System.out.println("ZDT 3\n");
			solutionType = "ArrayReal";
		    numberOfVariables = 30;
		    problem = new ZDT3(solutionType, numberOfVariables);
		    
			break;
		case 4:
			System.out.println("ZDT 4\n");
			solutionType = "ArrayReal";
		    numberOfVariables = 10;
		    problem = new ZDT4(solutionType, numberOfVariables);
		    
			break;
		case 5:
			System.out.println("ZDT 5\n");
			solutionType = "Binary";
		    numberOfVariables = 11;
		    problem = new ZDT5(solutionType, numberOfVariables);
		    
			break;
		case 6:
			System.out.println("ZDT 6\n");
			solutionType = "ArrayReal";
		    numberOfVariables = 10;
		    problem = new ZDT6(solutionType, numberOfVariables);
			
			break;

		default:
			break;
		}
    	
    	indicators = null ;
    	//indicators = new QualityIndicator(problem, "VAR");
        algorithm = new NSGAII(problem);
        //algorithm = new ssNSGAII(problem);

        // Algorithm parameters
        algorithm.setInputParameter("populationSize",POPULATION_SIZE);
        algorithm.setInputParameter("maxEvaluations",MAX_EVALUATIONS);

        if(i==5){ //Para el caso del ZDT5
        	// Mutation and Crossover Binary codification 
            parameters = new HashMap() ;
            parameters.put("probability", CROSSOVER_RATE) ;
            crossover = CrossoverFactory.getCrossoverOperator("SinglePointCrossover", parameters);                   
            crossover.setParameter("probability",CROSSOVER_RATE);                   
            
            parameters = new HashMap() ;
            parameters.put("probability", MUTATION_RATE) ;
            mutation = MutationFactory.getMutationOperator("BitFlipMutation", parameters);                    
            
            // Selection Operator 
            parameters = null ;
            selection = new BinaryTournament(parameters); 
        }
        else{
        	// Mutation and Crossover for Real codification 
            parameters = new HashMap() ;
            parameters.put("probability", CROSSOVER_RATE) ;
            parameters.put("distributionIndex", 20.0) ;
            crossover = CrossoverFactory.getCrossoverOperator("SBXCrossover", parameters);                   

            parameters = new HashMap() ;
            //parameters.put("probability", 1.0/problem.getNumberOfVariables()) ;
            parameters.put("probability", MUTATION_RATE) ;
            parameters.put("distributionIndex", 20.0) ;
            mutation = MutationFactory.getMutationOperator("PolynomialMutation", parameters);                    

            // Selection Operator 
            parameters = null ;
            selection = SelectionFactory.getSelectionOperator("BinaryTournament2", parameters) ;
        }
                                   

        // Add the operators to the algorithm
        algorithm.addOperator("crossover",crossover);
        algorithm.addOperator("mutation",mutation);
        algorithm.addOperator("selection",selection);

        // Add the indicator object to the algorithm
        algorithm.setInputParameter("indicators", indicators) ;
        
        // Execute the Algorithm
        long initTime = System.currentTimeMillis();
        SolutionSet population = algorithm.execute();
        long estimatedTime = System.currentTimeMillis() - initTime;
        
        // Result messages 
        //logger_.info("Tiempo total de ejecuci�n - "+algorithm.getProblem().getName()+": " +estimatedTime + "ms");
        System.out.println("Tiempo total de ejecuci�n - "+algorithm.getProblem().getName()+": " +estimatedTime + "ms");
        bw.write("Tiempo total de ejecuci�n - "+algorithm.getProblem().getName()+": " +estimatedTime + "ms\n");
        bw.newLine();
        
        //VAR = Pareto optimal solutions
        paretoTruePathFile = "C:/"+algorithm.getProblem().getName()+"-"+"Pareto_Optimal_Solutions_"+date;
        //logger_.info("Variables values have been writen to file C:/"+algorithm.getProblem().getName()+"-"+"Pareto_Optimal_Solutions_"+date);
        System.out.println("Variables values have been writen to file "+paretoTruePathFile);
        bw.write("Variables values have been writen to file "+paretoTruePathFile);
        bw.newLine();
        
        population.printVariablesToFile(paretoTruePathFile);
        //population.printVariablesToFile("VAR");
        
        //FUN = Pareto front found by the algorithm
        paretoFoundPathFile = "C:/"+algorithm.getProblem().getName()+"-"+"Pareto_Front_Solutions_found_by_NSGAII_"+date;
        //logger_.info("Objectives values have been writen to file C:/"+algorithm.getProblem().getName()+"-"+"Pareto_Front_Solutions_found_by_NSGAII_"+date);
        
        System.out.println("Objectives values have been writen to file "+paretoFoundPathFile);
        bw.write("Objectives values have been writen to file "+paretoFoundPathFile);
        bw.newLine();
        
        population.printObjectivesToFile(paretoFoundPathFile);
        //population.printObjectivesToFile("FUN");
      
        indicators = new QualityIndicator(problem, paretoFoundPathFile);
        if (indicators != null) {
          //logger_.info("Quality indicators - "+algorithm.getProblem().getName()) ;
          System.out.println("Quality indicators - "+algorithm.getProblem().getName());
          bw.write("Quality indicators - "+algorithm.getProblem().getName());
          bw.newLine();
          
          //logger_.info("Hypervolume - "+algorithm.getProblem().getName()+": " + indicators.getHypervolume(population)) ;
          System.out.println("Hypervolume - "+algorithm.getProblem().getName()+": " + indicators.getHypervolume(population));
          bw.write("Hypervolume - "+algorithm.getProblem().getName()+": " + indicators.getHypervolume(population));
          bw.newLine();
          
          //logger_.info("GD          - "+algorithm.getProblem().getName()+": " + indicators.getGD(population)) ;
          System.out.println("GD          - "+algorithm.getProblem().getName()+": " + indicators.getGD(population));
          bw.write("GD          - "+algorithm.getProblem().getName()+": " + indicators.getGD(population));
          bw.newLine();
          
          //logger_.info("IGD         - "+algorithm.getProblem().getName()+": " + indicators.getIGD(population)) ;
          //logger_.info("Spread      - "+algorithm.getProblem().getName()+": " + indicators.getSpread(population)) ;
          System.out.println("Spread      - "+algorithm.getProblem().getName()+": " + indicators.getSpread(population));
          bw.write("Spread      - "+algorithm.getProblem().getName()+": " + indicators.getSpread(population));
          bw.newLine();
          
          //logger_.info("Epsilon     - "+algorithm.getProblem().getName()+": " + indicators.getEpsilon(population)) ;  
         
          int evaluations = ((Integer)algorithm.getOutputParameter("evaluations")).intValue();
          //logger_.info("Velocidad       - "+algorithm.getProblem().getName()+": " + evaluations + " evaluaciones") ;
          //System.out.println("Velocidad       - "+algorithm.getProblem().getName()+": " + evaluations + " evaluaciones");
        } // if
    }//for
    bw.close();
  } //main
} // NSGAII_main
