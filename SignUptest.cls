public class SignUptest
{
public List<SelectOption> userlist{get;set;}
public static String inputValue { get; set; }
public static string selecteduser{get;set;}
  public String message {get;set;}
    public String getkeyval{get;set;}
     public String errormsg;
  

  public SignUptest() {
    
    message = 'Try again!!'; // initial message.
  }
    
 public String geterrormsg()
    {
       system.debug('geterrormsg '+errormsg); 
        return errormsg; 
    }
    
  public List<SelectOption> getusers()
  {
   userlist= new List<SelectOption>();
    list<Account> tempUserlist = [SELECT id,Name From Account where Status__c =: 'Active'] ;
for ( Account o : tempUserlist )
{
   userlist.add(new SelectOption(o.Id, o.Name));
    }
 system.debug('userlist' +userlist);
  return userlist; 

  }

  public void storePwd()
    {
    System.debug('inside storePwd');
     Id rec = Id.valueOf(selecteduser); 
        
     if(rec!= null && inputValue!= null )
        {
            Account  userRec = [select id,ActivationFlag__c,Password_flag__c from Account  where id =: rec];
            if(userRec.ActivationFlag__c==false){
            userRec.Password__c = inputValue; 
            userRec.Password_flag__c = true;    
            update userRec; 
                errormsg = 'NotActivated';
                system.debug('errormsg '+errormsg); 
            }
            else{
                system.debug('User already registered proceed login'); 
                errormsg = 'Activated'; 
            }
        }
    }
     public void validateData()

      {

      system.debug('inside validateData method'); 

      system.debug('getkeyval::::::'+getkeyval); 

      system.debug('selecteduser'+selecteduser); 

      Id i = Id.valueOf(selecteduser);

     

         if(selecteduser!= null && getkeyval!= null){

            Account  user = [select id, Name ,Key__c from Account  where id =: i];


          if(user.Key__c == getkeyval){

            user.ActivationFlag__c = true; 
            update user; 

           ApexPages.addMessage(new ApexPages.Message(ApexPages.Severity.INFO , 'sucess'));

          }

          else

          {

           ApexPages.addMessage(new ApexPages.Message(ApexPages.Severity.WARNING, 'key is not valid'));

          system.debug('invalid key');

          

          }

 

      }

     // return null;  

  }
}