package com.jhs.exam.exam2.http.controller;

import com.jhs.exam.exam2.container.ContainerComponent;
import com.jhs.exam.exam2.http.Rq;

public abstract class Controller implements ContainerComponent {
	public abstract void performAction(Rq rq);
}
