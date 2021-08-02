package com.jhs.exam.exam2.service;

import com.jhs.exam.exam2.container.Container;
import com.jhs.exam.exam2.container.ContainerComponent;
import com.jhs.exam.exam2.dto.Board;
import com.jhs.exam.exam2.repository.BoardRepository;

public class BoardService implements ContainerComponent {
	private BoardRepository boardRepository;

	public void init() {
		boardRepository = Container.boardRepository;
	}

	public Board getBoardById(int id) {
		return boardRepository.getBoardById(id);
	}
}