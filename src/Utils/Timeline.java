package Utils;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * This class is the clock for the system. It is designed to automatically handle the changes that occur to the accounts as time passes.
 * The class is made of two variables: lastUpdatedDate, which is the "current" time of the system. The second is serviceList, which is the list of services that are going to be updated when the clock increments
 */
public class Timeline {
    LocalDate lastUpdatedDate;//This time is primarily used by the system to check how much time has been passed since it has last been updated
    ArrayList<Services> serviceList = new ArrayList<Services>();
    
    public Timeline(){
        lastUpdatedDate = LocalDate.now();
    }
    public Timeline(LocalDate lastUpdatedDate){
        this.lastUpdatedDate = lastUpdatedDate;
    }
    public void advanceTime(int days){
        for(int x = 1; x <= days; x++){//For every day that passes:
            for(int i = 0; i < serviceList.size(); i++){//For every service in the system (or at least, the ones attached to the timeline)
                serviceList.get(i).updateTime(lastUpdatedDate, days);//Update the account with the new time
            }
            lastUpdatedDate = lastUpdatedDate.plusDays(1);//advance the clock forward a day
        }
    }
    public void setDate(LocalDate date){//Might need much stricter access attached to it in a real system, but handy for demonstrations
        this.lastUpdatedDate = date;
    }
    public void addServices(Services x){
        serviceList.add(x);
    }
    public void removeServices(Services x){
        serviceList.remove(x);
    }
    public LocalDate getLastUpdatedDate(){
        return lastUpdatedDate;
    }
}
