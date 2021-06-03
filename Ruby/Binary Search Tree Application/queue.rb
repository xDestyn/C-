class Queue
  attr_accessor(:elements, :size)
  def initialize
    @elements = []
    @size = 0
  end
  def enqueue(x)
    @elements << x
    @size += 1
    self
  end
  def dequeue
    if size > 0
      @size -= 1
      return @elements.shift
    end
  end
  def size
    return @size
  end

  # dup does a shallow copy of the receiver
  def dup
    copy = Queue.new
    copy.size = @size
    copy.elements = @elements
    copy
  end

  def clone
    copy = Queue.new
    copy.size = @size
    elements = Marshal.dump(@elements)
    elements = Marshal.load(elements)
    copy.elements = elements
    copy
  end

  def to_s
    return "ELements == #{@elements}; size == #{@size}."
  end
end

a = Queue.new
b = a
a.enqueue(10)
a.enqueue(["hello", "there",200])
a.enqueue(30)
c = a.clone
puts a
puts b
puts c
puts [1, 2, 3]
p [1, 2, 3]
a.elements[1][1] = "Ugo"
puts a
puts b
puts c
puts a.dequeue


