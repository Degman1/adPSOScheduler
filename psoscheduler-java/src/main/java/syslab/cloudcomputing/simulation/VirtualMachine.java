package syslab.cloudcomputing.simulation;

/*
 * VirtualMachine represents a single virtual machine that sections off a portion of a
 * single machine in a data center
 */
public class VirtualMachine {
  private static int idCounter = 1;
	private int id;
	private int dataCenterId;
	private int millionsOfInstructionsPerSecond;
	private double activeStateJoulesPerMillionInstructions;

	public VirtualMachine(int millionsOfInstructionsPerSecond) {
		this.id = VirtualMachine.idCounter++;
		this.millionsOfInstructionsPerSecond = millionsOfInstructionsPerSecond;
		this.activeStateJoulesPerMillionInstructions = 500;
		this.dataCenterId = 0;
	}

	public VirtualMachine(int millionsOfInstructionsPerSecond, double activeStateJoulesPerMillionInstructions) {
		this.id = VirtualMachine.idCounter++;
		this.millionsOfInstructionsPerSecond = millionsOfInstructionsPerSecond;
		this.activeStateJoulesPerMillionInstructions = activeStateJoulesPerMillionInstructions;
		this.dataCenterId = 0;
	}

	public double getActiveStateJoulesPerMillionInstructions() {
		return this.activeStateJoulesPerMillionInstructions;
	}

	public double getIdleStateJoulesPerMillionInstructions() {
		// According to https://pdf.sciencedirectassets.com/280416/1-s2.0-S1319157820X00024/1-s2.0-S1319157817303361/main.pdf?X-Amz-Security-Token=IQoJb3JpZ2luX2VjEGEaCXVzLWVhc3QtMSJHMEUCIAnFYurY1Gus9viOlcuoWEPxbszjPz4HQywtBp4gRh2GAiEAvCwsSvysGBgk38ovbjyhKrNg3VQ7falBDsL%2Ffm1nqVUqvAUImv%2F%2F%2F%2F%2F%2F%2F%2F%2F%2FARAFGgwwNTkwMDM1NDY4NjUiDGcVxoQOmcec9UawiCqQBW3bNWEVUrCl35LbcAN1dLjqbZn01QwmpbbU2bjRA8J9EpXK1akvweU%2FwpxmEETHuQX8xXJeoWZk7QTSshMpT8cvEBmLG4HP0Knm4eyZAnB2WMRwuM3gUOubgBo1Bk5sWCpHQD2mpFJCe9NdnxonfqgebtDHRk68R%2BEJlSA14HkYQxvO3lVlg0hYrIUWV%2FP22SOw%2Bu3jSgQXkFh8jNHg884mxOwPSm%2F%2Fi60qXfDqGT9JnHjZ45FEQlg%2FD2T70Fq7Y74S6idhaq26CZ%2FzmVPJE%2BHgTsM3xbZTk9pJ6ewwSUHOxBKdmWHAwTbYBD4V2fBTPHYl96%2BaQNXtfnIOlkUnrXIBK8cfq4n3ak6xhwJ4p7fq8J7nYHIG1W5iA5MRgqigydWMa2kQQlrJPQwZy%2BzEp4n3fo7tc0E5pGqn70qGYUJVXKVXScfxont8PO812GbgXFuyJ3tyvIVdmb%2BYWQ1PSWb9cBDilrwT8DU5oqVTYGK7smM635ymN2LI%2BOsJ0tNFEJkACKTL8VrMo1qvlFi%2BtRzCeFdIestbSe6dVT1FeBtlPkm71xnj1rwp20oh94UjBClbruYulMteGIGa7jL9QUN33Miy6YcRzZxM6xepF36hXYz9zfVndFe6%2BaU9MdUn6eVYe8LaP%2BhGD5ReyVlR6ojss2R692fxnVxDdjUmKjjufpS2FkZEth7sUe2CdH3j6lyk7EldzztaDfu2EyeSgCj8YpxNDqvFYhO4l5HRutYG7EcqWb0qCoTr1S79SUOG0B%2Br2b2dBUYQDezsfFmA0ZT65DjdHaXScAZePIVgj%2FwBWwjGhtcZY5u1BoZ5wH4hUPqeqBvW73CtxL6Jsi7BwRiUjk44GSeBd6mKTSZRq%2BQmMJiHpqoGOrEBQGG%2FjXIrv2ZN2RY2l4NKeKTUcOLpR4EzE8sV23YtG51mF6kGX35v9aivZEwWqPZawpXx568%2F6vAcdM41WD1%2BcoLDzWV2DkmAcwO30NqnOR6CKVsnyhvXOfnbVKV24Ay7IKgxvERi%2BHL8x4%2F83GBCB3Wt%2BJXBGBFstvrpJjU6AVy4XxIVfHXp8nP%2BD7N3yU5y5JRkLZwgAHePh2PPlcVwwx%2F1CfYRuAu2pBlKWtTR8cq2&X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Date=20231107T003741Z&X-Amz-SignedHeaders=host&X-Amz-Expires=300&X-Amz-Credential=ASIAQ3PHCVTYQLJ5R7PC%2F20231107%2Fus-east-1%2Fs3%2Faws4_request&X-Amz-Signature=990bc81ef32fb65c2e80efca27d3ec0328bcd903c6e9e5512ec0a4746453ce9c&hash=47ee638ef3e8fbf8d3fe647506afbc3712143b9210ac8e0b20c33ae2b38f120f&host=68042c943591013ac2b2430a89b270f6af2c76d8dfd086a07176afe7c76c2c61&pii=S1319157817303361&tid=spdf-216d5cd9-c2e1-44b6-a4fc-059dcfd86f5e&sid=2927ae6c23c719410f2a51a784616f99fc1agxrqa&type=client&tsoh=d3d3LnNjaWVuY2VkaXJlY3QuY29t&ua=131c585d53555b5556&rr=8221825779b64cd4&cc=us
		return this.activeStateJoulesPerMillionInstructions * 0.6;
	}
	
	public int hashCode() {
		return id;
	}
	
	public boolean equals(VirtualMachine otherVM) {
		return this.id == otherVM.id;
	}

  public String toString() {
    return String.format("(VM%d: %d mips, %d J/MI)", this.id, this.millionsOfInstructionsPerSecond, this.activeStateJoulesPerMillionInstructions);
  }

  public int getId() {
    return this.id;
  }

  public int getMillionsOfInstructionsPerSecond() {
    return this.millionsOfInstructionsPerSecond;
  }

	public int getDataCenterId() {
		return this.dataCenterId;
	}

	public void setDataCenterId(int dataCenterId) {
		this.dataCenterId = dataCenterId;
	}

	public void setMillionsOfInstructionsPerSecond(int millionsOfInstructionsPerSecond) {
		this.millionsOfInstructionsPerSecond = millionsOfInstructionsPerSecond;
	}
}
