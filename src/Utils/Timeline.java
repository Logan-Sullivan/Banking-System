package Utils;

import java.time.LocalDate;
import java.util.ArrayList;

//TODO: Check this works for loans, and apply to accounts. After should look into next steps, those being: Update customer.csv with new data, and updating UI (Get help for this part? Commit early?)
//Plan for doing this: merge prototype branch to this branch, fix problems between them, then make pull request for prototype

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
        // boolean isMonthRollover = false;
        // if(lastUpdatedDate.plusDays(days).isAfter(LocalDate.of(lastUpdatedDate.getYear(), lastUpdatedDate.getMonth(), lastUpdatedDate.lengthOfMonth()))){
        //     isMonthRollover = true;
        // }
        lastUpdatedDate = lastUpdatedDate.plusDays(days);
        
        for(int i = 0; i < serviceList.size(); i++){
            serviceList.get(i).updateTime(lastUpdatedDate, days);
        }
        
    }
    public void setDate(LocalDate date){//Might need much stricter access attached to it in a real system, but handy for demonstrations
        this.lastUpdatedDate = date;
    }
}
