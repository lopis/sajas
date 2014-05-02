package repast;

import repast.simphony.engine.schedule.Schedule;
import repast.simphony.engine.schedule.ScheduleParameters;
import up.fe.liacc.repacl.AbstractAgent;
import up.fe.liacc.repacl.Context;
import up.fe.liacc.repacl.behaviour.Behaviour;

public class RepastAgent extends AbstractAgent {
	
	@Override
	protected void addBehavior(Behaviour behaviour) {
		ScheduleParameters params = ScheduleParameters.createRepeating(1, 1);
		Schedule schedule = new Schedule();
		schedule.schedule(params, behaviour, "action");
		
		Context.getContext().add(behaviour);
	}

}
