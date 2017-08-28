public class LoginTestController2
{
public List<SelectOption> userlist{get;set;}
public static String inputValue { get; set; }
public static String selecteduser{get;set;}
public List<EventRegContent> lstaddedEvent{get;set;}
// public static String selAccountName; 
public String userSelectedAcc; 


    public String message {get;set;}
 
  public LoginTestController2() {
    system.debug('LoginTestController2');
  //  getfullDetails();
  }

  public List<SelectOption> getusers()
  {
   userlist= new List<SelectOption>();
    list<Account> tempUserlist = [SELECT id,Name From Account] ;
for ( Account o : tempUserlist )
{
   userlist.add(new SelectOption(o.Id, o.Name));
    }
  return userlist; 

  }

  
     public PageReference validateData()

      {

      system.debug('inside validateData method'); 

      Id i = Id.valueOf(selecteduser);

     

         if(i!= null && inputValue!= null){

            Account  user = [select id, Name ,Password__c from Account  where id =: i];


          if(user.Password__c == inputValue){

     

           ApexPages.addMessage(new ApexPages.Message(ApexPages.Severity.INFO , 'sucess'));
    //       PageReference registerEvents = Page.registerEvents;
    //       registerEvents.setRedirect(true);
    //       return registerEvents;
     
    //      return Page.registerEvents.setRedirect(false);
    
    /*      PageReference pageRef= new PageReference('/apex/registerEvents');
            pageRef.setRedirect(false);       
            return pageRef;  */
            
            PageReference pageRef= new PageReference('/apex/registerEvents2');
            pageRef.getParameters().put('selAccountName',selecteduser)  ;
            pageRef.setRedirect(true);       
            return pageRef;  
          }

          else

          {

           ApexPages.addMessage(new ApexPages.Message(ApexPages.Severity.WARNING, 'Incorrect Password'));        

          }

 

      }

      return null;  

  }
    
    // Part 2 starts here 
    // Declaration part starts here 
  //     public List<EventRegContent> lstEvent{get;set;}
    List<EventRegContent> lstEvent = new List<EventRegContent>(); 
    // Declaration part ends here 
    
    public String getshowUserName()
    {
        userSelectedAcc = System.currentPagereference().getParameters().get('selAccountName');
        Id accId3 = Id.valueOf(userSelectedAcc);
        Account  userAcc3 = [select id, Name ,Password__c from Account  where id =: accId3];
        String userName = userAcc3.Name; 
        return userName; 
    }
    
    public List<EventRegContent> getlstEvent()
        {
      system.debug('getlstEvent'); 
       getfullDetails();
      return lstEvent; 
        }
    
    public void setlstEvent(List<EventRegContent> lstEvent)
    {
        System.debug('setlstEvent'); 
        this.lstEvent = lstEvent; 
    }
    
     public List<EventRegContent> getlstaddedEvent()
        {
      system.debug('getlstaddedEvent'); 
      getfullDetails();
      return lstaddedEvent; 
        }

        private void getfullDetails()
        {
      lstEvent = new List<EventRegContent>(); 
      lstaddedEvent= new List<EventRegContent>();  
      system.debug('getfullDetails') ;
      userSelectedAcc = System.currentPagereference().getParameters().get('selAccountName');  
      Id accId = Id.valueOf(userSelectedAcc);
      List<Registered_Event__c> tempRegevnts = new List<Registered_Event__c>();     
      Map<String,Registered_Event__c> accountMap = new Map<String,Registered_Event__c>();  
      List<Events__c> allEvents = [select id,Name,Event_Date__c,Available_Event_Slots__c from Events__c ORDER BY Event_Date__c ASC];   
      tempRegevnts = [SELECT Name, Account__c, Event__c FROM Registered_Event__c where Account__c=: accId];  
      for(Registered_Event__c eachRegEvnt : tempRegevnts)
      {
          Id eventID1 = Id.valueOf(eachRegEvnt.Event__c); 
          Events__c eventRec1 = [SELECT id, Name FROM Events__c where Id =: eventID1];
          accountMap.put(eventRec1.Name,eachRegEvnt); 
      }
      for(Events__c eachEvent : allEvents)
      {
          EventRegContent evContent = new EventRegContent();  
          evContent.EventName = eachEvent.Name; 
          // Logic to populate event date, event available slots begins here 
          evContent.EventDate = eachEvent.Event_Date__c.format('MM/dd/yyyy h:mm a'); 
          evContent.availableSlots = eachEvent.Available_Event_Slots__c; 
          // Logic to populate event date, event available slots ends here  
          evContent.Selected = true; 
          String validateEvent = evContent.EventName;           
          if(accountMap.containsKey(validateEvent))
          {   
              Registered_Event__c registeredEvnt = accountMap.get(validateEvent); 
          //    evContent.EventID = registeredEvnt.Id; 
             evContent.EventID = eachEvent.Id; 
              evContent.Selected = true; 
              lstaddedEvent.add(evContent); 
              evContent.IsAdded = true; 
              evContent.IsRemoved = false;
              evContent.statusColor = 'blue';
              evContent.status = 'Registered';
          }
          else
          {
              evContent.Selected = false; 
              evContent.IsAdded = false; 
              evContent.IsRemoved = false; 
              evContent.EventID = eachEvent.Id;
              if(evContent.availableSlots > 0)
              {    
              evContent.statusColor = 'green';
              evContent.status = 'Available for Registration';    
              }
              else
              {
               evContent.statusColor = 'red';   
               evContent.status = 'Contact Admin for Registration';   
              }    
          }
          System.debug('eachEvent.Id'+eachEvent.Id); 
          System.debug('evContent.EventID'+evContent.EventID); 
          lstEvent.add(evContent); 
      }
        
      
              
        }
    
    // Inserting the registered event into the table 
    public PageReference doAddEvent() {
        System.debug('doAddEvent'); 
        boolean nonRegEventFlag = false; 
        String nonRegEvent = ''; 
        list<Registered_Event__c> addedEvents = new list<Registered_Event__c>();
        list<String> nonRegEventList = new list<String>(); 
        // Pulling the primary contact details starts here
        Id accId5 = Id.valueOf(userSelectedAcc);
        Account  userAcc5 = [select id, Name ,Primary_contact__c from Account  where id =: accId5];
        Contact contactDet2 = [select id, Name, Email from Contact where id =: userAcc5.Primary_contact__c];
        // Pulling the primary contact details ends here
        for(EventRegContent tempDispEvent : lstEvent)
        {
            System.debug('tempDispEvent.EventName' +tempDispEvent.EventName); 
            System.debug('tempDispEvent.EventID' +tempDispEvent.EventID); 
            System.debug('tempDispEvent.Selected' +tempDispEvent.Selected); 
            System.debug('tempDispEvent.IsAdded' +tempDispEvent.IsAdded); 
        }    
        for(EventRegContent checkedEvent : lstEvent)
        {
            if(checkedEvent.Selected)
            {
                if(!checkedEvent.IsAdded)
                {    
       //         Events__c selectedEve = new Events__c(); 
                System.debug('checkedEvent.EventID'+checkedEvent.EventID); 
       //         selectedEve.id = checkedEvent.EventID; 
       //         System.debug('selectedEve.id'+selectedEve.id); 
       //         Events__c ev2 = [select Name,Available_Event_Slots__c from Events__c where id =: selectedEve.id];
                Events__c ev2 = [select Name,Available_Event_Slots__c from Events__c where id =: checkedEvent.EventID]; 
                if(ev2.Available_Event_Slots__c > 0)
                {    
                // logic for finding available slots end here
                Registered_Event__c addSingRec = new Registered_Event__c(); 
                addSingRec.Account__c = userSelectedAcc; 
                addSingRec.Primary_Contact_Email__c = contactDet2.Email;
                addSingRec.Event__c = ev2.Id;
                addedEvents.add(addSingRec);
                }
                else
                {
                nonRegEventList.add(ev2.Name); 
                }
                }    
            }
        }
        insert addedEvents; 
        // Displaying non registered event names starts here 
        if(!(nonRegEventList.isEmpty()))
        {
            nonRegEventFlag = true; 
            if(nonRegEventList.size() > 1)
            {
                for(Integer i = 0; i<nonRegEventList.size(); i++ )
                {
                    
                    nonRegEvent = nonRegEvent + nonRegEventList.get(i);
                    if(i!=nonRegEventList.size()-1)
                    {    
                    nonRegEvent = nonRegEvent + ', '; 
                    }
                }
                
            }
            else
            {
                nonRegEvent = nonRegEventList.get(0) ;
            }
            nonRegEvent = nonRegEvent + ' not registered. Available slot is 0, so please contact Admin ' ; 
        }
        // Displaying non registered event names ends here 
        if(nonRegEventFlag)
        {    
        ApexPages.addMessage(new ApexPages.Message(ApexPages.Severity.INFO , nonRegEvent));
        }
        else
        {
            ApexPages.addMessage(new ApexPages.Message(ApexPages.Severity.INFO , 'Sucessfully Registered for the Event'));
        }
   //    getfullDetails();
     return null;    
    }

        // Removing the registered events starts here   
        public void doRemoveEvent() {
     list<Registered_Event__c> removeEveList = new list<Registered_Event__c>();
     list<String> removdEventNames = new list<String>(); 
        String removdEveName = ''; 
       for ( EventRegContent  checkedEvent2 : lstaddedEvent)
       {
            if( checkedEvent2.IsRemoved)
            {
                Registered_Event__c eventToRemove = new Registered_Event__c(); 
                Registered_Event__c registeredEvent2 = [select id from Registered_Event__c where Account__c =: userSelectedAcc and Event__c =: checkedEvent2.EventID]; 
                eventToRemove.Id = registeredEvent2.Id;  
                removeEveList.add(eventToRemove); 
                Events__c ev3 = [select Name,Available_Event_Slots__c from Events__c where id =: checkedEvent2.EventID]; 
                removdEventNames.add(ev3.Name);
            }
       }
        
     delete removeEveList;
        // Displaying removed event names starts here 
        if(!(removdEventNames.isEmpty()))
        {
            if(removdEventNames.size() > 1)
            {
                for(Integer i = 0; i<removdEventNames.size(); i++ )
                {
                    
                    removdEveName = removdEveName + removdEventNames.get(i);
                    if(i!=removdEventNames.size()-1)
                    {    
                    removdEveName = removdEveName + ', '; 
                    }
                }
                
            }
            else
            {
                removdEveName = removdEventNames.get(0) ;
            }
            removdEveName = removdEveName + ' removed from registered event ' ; 
             ApexPages.addMessage(new ApexPages.Message(ApexPages.Severity.INFO , removdEveName));
        }
        // Displaying removed event names ends here 
  

   }  
        // Removing the registered events ends here 
    
    // Part 2 ends here    
}