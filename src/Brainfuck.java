import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;

public class Brainfuck {
	byte[] data;
	HashMap<Integer, Integer> jumps = new HashMap<Integer, Integer>();
	int dataptr;
	int instptr;
	char[] instructions;
	Queue<Byte> input = new LinkedList<Byte>();

	public Brainfuck(String program, int numberOfCells) {
		program.replaceAll("[^\\Q<>[],.+-\\E]", "");
		instructions = program.toCharArray();
		Stack<Integer> brackets = new Stack<Integer>();
		for(int i= 0; i<instructions.length;i++){
			char c = instructions[i];
			if(c == '[')
				brackets.push(i);
			if(c == ']') {
				int left = brackets.pop();
				int right = i;
				jumps.put(left, right+1);
				jumps.put(right, left+1);
			}

		}
		data = new byte[numberOfCells];
	}

	public void run() {
		while(instptr < instructions.length) {
			switch(instructions[instptr]) {
			case '<': left(); instptr++; break;
			case '>': right(); instptr++; break;
			case '+': increment(); instptr++; break;
			case '-': decrement(); instptr++; break;
			case '[': jump(); break;
			case ']': jumpback(); break;
			case '.': output(); instptr++; break;
			case ',': input(); instptr++; break;
			default: instptr++;
			}
		}
	}

	private void right() {
		dataptr++;
	}

	private void left() {
		dataptr--;
	}

	private void increment() {
		data[dataptr]++;
	}

	private void decrement() {
		data[dataptr]--;
	}

	private void output() {
		//hack um bytes in char zu verwandeln
		byte[] arr = new byte[1];
		arr[0]=data[dataptr];
		String s = "xXx";
		try {
			s = new String(arr, "US-ASCII");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.print(s);

	}

	private void input() {
		if (input.isEmpty()) {
			String s = new Scanner(System.in).nextLine();
			for(int i = 0; i<s.length(); i++) {
				int ascii = (int) s.charAt(i);
				byte b = (byte) ascii;
				input.add(b);
			}
			input();

//			input.add((byte) 3);
//			input.add((byte) 4);
//			input.add((byte) 5);
		} else {
			data[dataptr] = input.remove();
		}
	}

	private void jump () {
		if(data[dataptr] == 0)
			instptr = jumps.get(instptr);
		else
			instptr++;
	}

	private void jumpback () {
		if(data[dataptr] != 0)
			instptr = jumps.get(instptr);
		else
			instptr++;
	}

}
