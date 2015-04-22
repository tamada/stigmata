package com.github.stigmata;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Haruaki TAMADA
 */
public class BirthmarkExtractionFailedException extends BirthmarkException {
       private static final long serialVersionUID = 21932436457235L;

       private List<Throwable> causes = new ArrayList<Throwable>();

       public BirthmarkExtractionFailedException() {
               super();
       }

       public BirthmarkExtractionFailedException(String arg0, Throwable cause) {
               super(arg0, cause);
       }

       public BirthmarkExtractionFailedException(String arg0) {
               super(arg0);
       }

       public BirthmarkExtractionFailedException(Throwable cause) {
               super(cause);
       }

       public boolean isFailed(){
               return causes.size() != 0;
       }

       public void addCause(Throwable cause){
               causes.add(cause);
       }

       public void addCauses(Throwable[] causeList){
               for(Throwable throwable: causeList){
                       causes.add(throwable);
               }
       }

       public Throwable[] getCauses(){
               return causes.toArray(new Throwable[causes.size()]);
       }
}