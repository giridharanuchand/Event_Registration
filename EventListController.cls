public with sharing class EventListController{

    //All these fields are accessed on the front-end. They are referenced directly without need of any object
    
    
     
    public String showOther {
        get;
        set;
    }
    
    //Following are all the form fields on eventUI1 visualforce page
    public Integer people_count {
        get;
        set;
    }
    public Integer fulltime_positions {
        get;
        set;
    }
    public Integer intern_positions {
        get;
        set;
    }
    public String contactPhone {
        get;
        set;
    }
    public String contactTitle {
        get;
        set;
    }
    public String contactEmail {
        get;
        set;
    }
    public String lastName {
        get;
        set;
    }
    public String firstName {
        get;
        set;
    }
    public String street {
        get;
        set;
    }
    public String city {
        get;
        set;
    }
    public String state {
        get;
        set;
    }
    public String country {
        get;
        set;
    }
    public String zip {
        get;
        set;
    }
    
    //On eventUI1 page, once an account is selected, it is bound to this variable throughout the session. The value is changed when some other account is selected
    public String selAccount {
        get;
        set;
    }
    
    //On eventUI1 page, once contact is selected from the dropdown or if a new contact is created, it is bound to this variable throughout the session.
    public String selContact {
        get;
        set;
    }
    
    //Dropdown list containing list of all contacts
    public List < SelectOption > contactOptions {
        get;
        set;
    }
    
    //Create an instance of selected contact
    public Contact contactObj {
        get;
        set;
    }
    
    //Event id value is stored in this field after user clicks on calendar event.
    public String selectedEventId {
        get;
        set;
    }
    public Boolean includeMyEvents {
        get;
        set;
    }
    
    //List of events to be displayed on calendar
    public list < calEvent > events {
        get;
        set;
    }
    public list < calEvent > oldEvents {
        get;
        set;
    }
    
    // Fall 2016 change starts here 
    public List < eventDetails > listAllEvents {
        get;
        set;
    }
    // Fall 2016 change ends here 
    String dtFormat = 'EEE, d MMM yyyy h:mm a ';

    String impdtFormat = 'EEE, d MMM yyyy';

    String displayDtFormat = 'MM/dd/yyyy';
    
    //Class for displaying Calendar events.
    public class calEvent {
    public List < Event_Registration__c > registeredAccount{
              get;
              set;
    
    }
  
        //All these fields are accessed on visualforce page using JQuery. This is for using FullCalendar API which is free open source. For documentation http://fullcalendar.io/  
   
        public String title {
            get;
            set;
        }
        public String evType {
            get;
            set;
        }
        public String id {
            get;
            set;
        }
        public String startString {
            get;
            set;
        }
        public String className {
            get;
            set;
        }
        
        public String contactName {
            get;
            set;
        }
        public String link {
            get;
            set;
        }
        public String Room {
        get;
        set;
        }
      /*  public List<Event_Registration__c> registeredAccount {
        get;
        set;
        }*/
    }
    
     // Fall 2016 change starts here 
    public class eventDetails {
        public String eveName {
            get;
            set;
        }
      public String eveDate {
            get;
            set;
        }    
      public String eveDescription {
            get;
            set;
        }      
      public String eveRoom {
        get;
        set;
        }  
      public String eveColor {
            get;
            set;
        }
    }    
    // Fall 2016 change ends here 

    //Class constructor. Everytime visualforce page is refreshed, class constructor is evoked.
    public EventListController() {

        contactObj = new Contact();
        showOther = 'false';
        //Refresh list with events
        loadEvents();
    }
    
    //Load our events to Calendar event list.
    public void loadEvents() {
        oldEvents = new list < calEvent > ();
        
        //Get all events from DummyEvent table. Update the query based on semester. For Fall 16 events get the events with date greater than June 1st
        for (DummyEvent__c ev: [select id,name, Event_count__C, Event_datetime__c, link__c, Event_Type__c, Room__c from DummyEvent__c where Event_datetime__C > 2016-06-01T16:00:00.000+0000 order by Event_datetime__c]) {
            
      
       List < Event_Registration__c > registeredAccount = new List < Event_Registration__c > ();
            //For mapping accounts who have already registered for a particular event and displaying it on calendar. Currently not in use.
            registeredAccount = [SELECT Account__r.name FROM Event_Registration__c where Name = :ev.name];
           
           List < Account > registeredAccName = new List < Account > ();
                // Map all DummyEvent fields to calendar event fields. 
            DateTime startDT = ev.Event_datetime__c;
            calEvent myEvent = new calEvent();
            myEvent.title = ev.name;
            myEvent.startString = startDT.format(dtFormat);
            myEvent.link = ev.link__c;
            myEvent.evType = ev.Event_Type__c;
            myEvent.Room = ev.Room__c;
            myEvent.registeredAccount =registeredAccount;    
            Double c = ev.Event_Count__c;
            //event count is used for checking if we can register for the event or not
            //if count is 0, assign className as red and then on visualforce page handle it so that user can't select that event.
            if (c == 0) {
                myEvent.className = 'red';
            } else {
                myEvent.className = 'green';
            }
            //Add events to list so that they can be displayed on calendar.
            oldEvents.add(myEvent);
        }
        
        // Fall 2016 change starts here 
        listAllEvents = new list < eventDetails > ();
        List<Events__c> allEvents = [select id,Name,Event_Date__c,Available_Event_Slots__c, Room__c, Description__c from Events__c ORDER BY Event_Date__c ASC];
        for(Events__c eachEvent : allEvents)
        {
                        eventDetails currEvent = new eventDetails(); 
            currEvent.eveName = eachEvent.Name; 
            DateTime eveDate1 = eachEvent.Event_Date__c;
            currEvent.eveDate = eveDate1.format(dtFormat);
            currEvent.eveRoom = eachEvent.Room__c; 
            currEvent.eveDescription = eachEvent.Description__c; 
            if(eachEvent.Available_Event_Slots__c == 0)
            {
                currEvent.eveColor = 'red'; 
            }
            else
            {
                currEvent.eveColor = 'green';
            }
            
                        listAllEvents.add(currEvent); 
        }    
        // Fall 2016 change ends here 
    }
        
           
   
    
}
