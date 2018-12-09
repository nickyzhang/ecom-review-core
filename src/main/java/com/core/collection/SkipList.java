package com.core.collection;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

public class SkipList<E> {
    static final int HEAD_KEY = 0x80000000;
    static final int TAIL_KEY = 0x7fffffff;
    Node<E> head,tail;
    int level = 0,size = 0;
    int maxLevel;
    double ratio; // 是否增加层数概率值
    Random random = new Random(); // 用于产生随机概率

    public SkipList() {
        this(4);
    }

    public SkipList(int maxLevel) {
        this.maxLevel = maxLevel;
        this.ratio = 0.5;
        this.head = new Node<>(HEAD_KEY,null);
        this.tail = new Node<>(TAIL_KEY,null);
        linkPrevNextNode(this.head, this.tail);
    }

    public SkipList(int maxLevel,double ratio) {
        this(maxLevel);
        this.ratio = ratio;
    }

    // 根据key查找节点
    public Node<E> find(int key) {
        Node<E> node = head;
        for (;;) {
            while (node.next.key != this.tail.key && node.next.key <= key) {
                node = node.next;
            }
            if (node.down == null) {
                break;
            }
            node = node.down;
        }
        return node;
    }

    // 查找元素
    public Node<E> get(int key) {
        if (empty()) return null;
        Node<E> node = find(key);
        if (node != null && node.key == key) {
            return node;
        }
        return null;
    }

    // 查询所有的节点
    public List<Node<E>> getAll() {
        if (empty()) return null;
        Node<E> node = this.head;
        while (node.down != null) {
            node = node.down;
        }
        List<Node<E>> nodeList = new ArrayList<>();
        while(node.next != null && node.next.key != this.tail.key) {
            nodeList.add(node.next);
            node = node.next;
        }
        return nodeList;
    }

    // 添加元素
    public void put(int key, E element) {
        // 是否已经存在这个key,如果存在则直接更新这个值
        Node<E> p = find(key);
        if (p != null && p.key == key) {
            update(p, element);
            return;
        }

        // 创建节点，添加在查找的节点后面
        Node<E> q = new Node<>(key,element);
        insertNode(p,q);

        // 这时候需要看是不是要增大层数
        int currentLevel = 0;// 当前层数
        // 如果小于概率值，则表示要增加；一般这个动作叫做抛硬币，这个概率是0.5
        while (random.nextDouble() < this.ratio && this.level < this.maxLevel) {
            if (currentLevel >= this.level) {
                this.level++;
                // 新增一层就需要增加新的都节点和尾节点
                Node<E> h = new Node<>(HEAD_KEY,null);
                Node<E> t = new Node<>(TAIL_KEY,null);
                linkPrevNextNode(h,t);
                linkUpDownNode(h,this.head);
                linkUpDownNode(t,this.tail);
                this.head = h;
                this.tail = t;
            }
            // 由于找到的节点p可能是由上指针也可能没有，所以紧接着就需要找到可以到上一层的节点，一般是往回找，即向后找
            // 注意这里肯定是可以找到的，最不济也就是头节点
            for(;p.up == null;){
                p = p.prev;
            }
            // 这时候更新p为找到的上一层节点，这时候的p可能是头节点，也可能不是
            p = p.up;

            // 新创建上层的需要插入的节点，注意，上层节点都是为了查找，所以不会操作其值之类，我们可以不初始化其值
            // 你实在想设置值也行
            Node<E> e = new Node<>(key,null);
            // 确立和p之间的关系
            insertNode(p,e);
            linkUpDownNode(e,q);
            q = e;
            currentLevel++;
        }
        this.size++;
    }

    // 更新跳跃链表
    public void update(Node<E> node,E element) {
        for (;;) {
            node.element = element;
            node = node.up;
            if (node == null) {
                break;
            }
        }
    }

    // 删除元素
    public void remove(int key) {
        for (Node<E> node = get(key);node != null; node = node.up) {
            deleteNode(node);
            this.size--;
        }
    }

    public boolean empty() {

        return this.size == 0;
    }

    public int size() {

        return this.size;
    }

    private void linkPrevNextNode(Node<E> prev, Node<E> next){
        prev.next = next;
        next.prev = prev;
    }

    private void linkUpDownNode(Node<E> up, Node<E> down){
        up.down = down;
        down.up = up;
    }

    private void insertNode(Node<E> prev, Node<E> current){
        prev.next.prev = current;
        current.next = prev.next;
        prev.next = current;
        current.prev = prev;
    }

    private void deleteNode(Node<E> current){
        current.prev.next = current.next;
        current.next.prev = current.prev;
    }



    static class Node<E> {
        int key;
        E element;
        Node<E> up,down,prev,next;

        public Node() {
        }

        public Node(int key, E element) {
            this.key = key;
            this.element = element;
        }
    }

    public static void main(String[] args) {
//        SkipList<Character> skipList = new SkipList<>(4,0.5);
//        skipList.put(15,'e');
//        skipList.put(60,'k');
//        skipList.put(5,'h');
//        skipList.put(38,'l');
//        skipList.put(88,'u');
//        skipList.put(44,'c');
//        skipList.put(44,'p');
//        skipList.put(99,'m');
//        skipList.remove(88);
//
//        System.out.println("跳跃链表的数量=> "+skipList.size);
//        for (Node<Character> node : skipList.getAll()) {
//            System.out.println("==> "+node.key +" : "+node.element);
//        }

        ConcurrentLinkedQueue<String> queue = new ConcurrentLinkedQueue<String>();
        queue.offer("hadoop");
        queue.offer("spark");
        queue.offer("flink");
    }
}
