package up.fe.liacc.repast;


import repast.simphony.engine.schedule.Schedule;
import repast.simphony.engine.schedule.ScheduleParameters;
import repast.simphony.util.ContextUtils;
import up.fe.liacc.sajas.core.Agent;
import up.fe.liacc.sajas.core.behaviours.Behaviour;

public class RepastAgent extends Agent {


	@Override
	protected void addBehaviour(Behaviour behaviour) {
		ScheduleParameters params = ScheduleParameters.createRepeating(1, 1);
		Schedule schedule = new Schedule();
		schedule.schedule(params, behaviour, "action");
		
		ContextUtils.getContext(this).add(behaviour);
		getBehaviours().add(behaviour);
	}

	@Override
	protected void removeBehaviour(Behaviour behaviour) {
		ContextUtils.getContext(this).remove(behaviour); // unschedule the behaviour
		getBehaviours().remove(behaviour);
	}

}

