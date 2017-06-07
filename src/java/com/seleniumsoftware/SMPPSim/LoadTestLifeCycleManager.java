package com.seleniumsoftware.SMPPSim;

import com.seleniumsoftware.SMPPSim.pdu.*;

import java.util.logging.*;

public class LoadTestLifeCycleManager extends LifeCycleManager {

private static Logger logger = Logger.getLogger("com.seleniumsoftware.smppsim");

	private Smsc smsc = Smsc.getInstance();

	private int discardThreshold;

	public LoadTestLifeCycleManager() {
		discardThreshold = SMPPSim.getDiscardFromQueueAfter();
		logger.finest("discardThreshold=" + discardThreshold);
	}

	public MessageState setState(MessageState m) {
		if (isTerminalState(m.getState()))
			return m;
		byte currentState = m.getState();
        String testmessage = new String(m.getPdu().getShort_message());
		if (testmessage.equalsIgnoreCase("EXPIRED")) {
			m.setState(PduConstants.EXPIRED);
			m.setErr(903);
		} else if (testmessage.equalsIgnoreCase("DELETED")) {
			m.setState(PduConstants.DELETED);
			m.setErr(904);
		} else if (testmessage.equalsIgnoreCase("UNDELIVERABLE")) {
			m.setState(PduConstants.UNDELIVERABLE);
			m.setErr(901);
		} else if (testmessage.equalsIgnoreCase("ACCEPTED")) {
			m.setState(PduConstants.ACCEPTED);
			m.setErr(2);
		} else if (testmessage.equalsIgnoreCase("REJECTED")) {
			m.setState(PduConstants.REJECTED);
			m.setErr(902);
		} else if (testmessage.equalsIgnoreCase("UNKNOWN")) {
            m.setState(PduConstants.UNKNOWN);
			m.setErr(905);
		} else {
			m.setState(PduConstants.DELIVERED);
			m.setErr(0);
		}
		if (isTerminalState(m.getState())) {
			m.setFinal_time(System.currentTimeMillis());
			// If delivery receipt requested prepare it....
			SubmitSM p = m.getPdu();
			logger.info("Message:"+p.getSeq_no()+" state="+getStateName(m.getState()));
			if (p.getRegistered_delivery_flag() == 1 && currentState != m.getState()) {
				prepDeliveryReceipt(m, p);
			} else {
				if (p.getRegistered_delivery_flag() == 2 && currentState != m.getState()) {
					if (isFailure(m.getState())) {
						prepDeliveryReceipt(m, p);
					}
				}
			}
		}
		return m;
	}

}