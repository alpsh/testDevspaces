// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package com.trilogy.fs.dms.directpayment;

import com.trilogy.ebb.lock.LockException;
import com.trilogy.ffc.html.FFCTracer;
import com.trilogy.fs.dms.core.*;
import com.trilogy.fs.dms.core.validator.*;
import com.trilogy.fs.util.DatabaseUtil;
import com.trilogy.html.gui.ext.date.DateUtil;
import com.trilogy.sc.*;

import java.util.*;



// Referenced classes of package com.trilogy.fs.dms.directpayment:
//            DirectPaymentsHelper

public class UHCFSDirectPaymentParticipationDetailsValidator extends FSDirectPaymentParticipationDetailsValidator
{
	public UHCFSDirectPaymentParticipationDetailsValidator(){
		
	}
    /* member class not found */
    class Errors {}
    
    public String getComment()
    {
        IFSDirectPaymentParticipationDetails ifsdirectpaymentparticipationdetails = (IFSDirectPaymentParticipationDetails)getSCObject();
        m_directPaymentComment=ifsdirectpaymentparticipationdetails.getPlanParticipationSegment().getComment();
        return m_directPaymentComment;
    }

    public void setComment(String s)
    {
    	IFSDirectPaymentParticipationDetails ifsdirectpaymentparticipationdetails = (IFSDirectPaymentParticipationDetails)getSCObject();
        m_directPaymentComment = s;
        ifsdirectpaymentparticipationdetails.getPlanParticipationSegment().setComment(m_directPaymentComment);
        
    }
	   
    protected Vector mandatoryValidateFather(ValidatorContext validatorcontext)
    throws ValidatorException
{
    Vector vector = new Vector();
    validateBBobjectStringFields(vector);
    validateRequiredProperties(vector);
    validateEnumerationProperties(vector);
    if(getSCObject() != null && getContainerValidator() != null && getContainerValidator().inSession() && getContainerPropertyName() != null)
        updateContainerValidator();
    return vector;
}
    
    protected Vector mandatoryValidate(ValidatorContext validatorcontext)
    {
        Vector vector = mandatoryValidateFather(validatorcontext);
        try{
        m_dpComp = getDirectPaymentForPlan(m_ppSeg.getPlan());
        if(m_dpComp == null)
            vector.add(getError("Errors.DirectPaymentParticipationDetails.InvalidComponent"));
        if(vector.size() != 0)
            return vector;
        m_errors.clear();
        IFSDirectPaymentParticipationDetails ifsdirectpaymentparticipationdetails = (IFSDirectPaymentParticipationDetails)getSCObject();
        if(ifsdirectpaymentparticipationdetails == null)
        {
            vector.add(getError("Errors.DirectPaymentParticipationDetails.InvalidDetailsObject"));
        } else
        {
        	ifsdirectpaymentparticipationdetails.getPlanParticipationSegment().setUHCOverrideFee(m_directPaymentOverrideFee);
            if(m_directPaymentOverrideFee != -1D)
            {
                ifsdirectpaymentparticipationdetails.setValue(m_directPaymentOverrideFee);
                ifsdirectpaymentparticipationdetails.setOverride(true);
            } else
            if(isNew(getSCObject()))
            {
                double d = calculateDirectPayment();
                ifsdirectpaymentparticipationdetails.setValue(d);
            }
            if(!(ifsdirectpaymentparticipationdetails.getValue() >= 0.0D||ifsdirectpaymentparticipationdetails.getValue()==-1.0D))
                vector.add(getError("Errors.DirectPaymentParticipationDetails.FeeLessThanZeroAndNotDefaultValue"));
            validateComponent();                                   
            if(ifsdirectpaymentparticipationdetails.getPlanParticipationSegment().getEndDate().before(ifsdirectpaymentparticipationdetails.getPlanParticipationSegment().getStartDate()))
            		vector.add(getError("Errors.DirectPaymentParticipationDetails.InvalidEndDateLaterThanStartDate"));
            FFCTracer.trace("###########Checking end date for new and existing records ################");
            if(ifsdirectpaymentparticipationdetails.getPlanParticipationSegment().getEndDate().before(DateUtil.today()))//||ifsdirectpaymentparticipationdetails.getPlanParticipationSegment().getStartDate().before(DateUtil.today()))      	
            		vector.add(getError("Errors.DirectPaymentParticipationDetails.StartDateAndEndDateCannotBePastDate"));
            if(isNew(ifsdirectpaymentparticipationdetails.getPlanParticipationSegment()))
            {
	            FFCTracer.trace("###########Checking of start and end dates since it is new record################");
	            if(ifsdirectpaymentparticipationdetails.getPlanParticipationSegment().getStartDate().before(DateUtil.today()))      	
	        		vector.add(getError("Errors.DirectPaymentParticipationDetails.StartDateAndEndDateCannotBePastDate"));
	            FFCTracer.trace("###########Done checking of dates################");
	        }
        }}
        catch(Exception exception){}
        vector.addAll(m_errors);
        return vector;
    }  
    
    @Override
    public void setSCObject(ISCObjectBase object) throws LockException
    {
		super.setSCObject(object, false);
	}
    
    public String m_directPaymentComment;
     
}