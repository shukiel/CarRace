package CarRaceMVC;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javafx.event.Event;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.paint.Color;

public class Car implements Defines {
	public manufacture getModel_id() {
		return model_id;
	}
	public void setModel_id(manufacture model_id) {
		this.model_id = model_id;
	}
	public Color getColor() {
		return color;
	}
	public void setColor(Color color) {
		this.color = color;
	}
	public size getCarSize() {
		return carSize;
	}
	public void setCarSize(size carSize) {
		this.carSize = carSize;
	}
	public double getSpeed() {
		return speed;
	}

	private int id;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	private manufacture model_id;
	private carType type;
	public carType getType() {
		return type;
	}
	public void setType(carType type) {
		this.type = type;
	}

	private CarLog log;
	private double speed;
	private Color color;
	private size carSize;
	private Map<eventType, ArrayList<EventHandler<Event>>> carHashMap;

	public Car(int id, int model_id, CarLog log, Color col, int carSize, int speed, int type) {
		this.id = id;
		this.model_id = manufacture.values()[model_id];
		this.log = log;
		this.speed = speed;
		this.color = col;
		this.type = carType.values()[type];
		this.carSize = size.values()[carSize];
		/*
		carHashMap = new HashMap<eventType, ArrayList<EventHandler<Event>>>();
		for (eventType et : eventType.values())
			carHashMap.put(et, new ArrayList<EventHandler<Event>>());
			*/
	}
	public void setSpeed(double speed) {
		this.speed = speed;
	}

	public synchronized void addEventHandler(EventHandler<Event> l, eventType et) {
		ArrayList<EventHandler<Event>> al;
		al = carHashMap.get(et);
		if (al == null)
			al = new ArrayList<EventHandler<Event>>();
		al.add(l);
		carHashMap.put(et, al);
	}

	public synchronized void removeEventHandler(EventHandler<Event> l,
			eventType et) {
		ArrayList<EventHandler<Event>> al;
		al = carHashMap.get(et);
		if (al != null && al.contains(l))
			al.remove(l);
		carHashMap.put(et, al);
	}

	private void processEvent(eventType et, Event e) {
		String msg;
		ArrayList<EventHandler<Event>> al;
		synchronized (this) {
			al = carHashMap.get(et);
			if (al == null)
				return;
		}
		msg = "";//TODO:: Set message to server
		log.printMsg(msg);
		for (int i = 0; i < al.size(); i++) {
			EventHandler<Event> handler = (EventHandler<Event>) al.get(i);
			handler.handle(e);
		}
	}
}