/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package socialpccontroller;

import java.util.Date;

/**
 *
 * @author Praveen
 */
public class accessToken {
    
    private String accessToken;
    private Date expiry;
    private boolean longLived;
    
    public accessToken(String a, Date b,boolean c)
    {
        accessToken=a;
        expiry=b;
        longLived=c;
    }
    public String getAccessToken()
    {
        return accessToken;
    }
    
    public Date getExpiryDate()
    {
      return expiry;   
    }
    public boolean isLongLived()
    {
        return longLived;
    }
}
