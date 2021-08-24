package com.jhs.exam.exam2.service;

import com.jhs.exam.exam2.container.Container;
import com.jhs.exam.exam2.container.ContainerComponent;
import com.jhs.exam.exam2.dto.Board;
import com.jhs.exam.exam2.repository.BoardRepository;

public class BoardService implements ContainerComponent {
	// BoardRepository객체를 사용하기 위해 사용
	private BoardRepository boardRepository;

	public void init() {
		boardRepository = Container.boardRepository;
	}

	// id번 board를 찾는 메서드
	public Board getBoardById(int id) {
		return boardRepository.getBoardById(id);
	}
}