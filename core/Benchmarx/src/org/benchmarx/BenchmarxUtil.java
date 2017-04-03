package org.benchmarx;

import java.io.IOException;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;

public class BenchmarxUtil<S,T, D> {

	private String folder;
	
	private BXTool<S, T, D> tool;
	
	public BenchmarxUtil(BXTool<S,T,D> tool){
		this.tool = tool;
	}
	
	@SuppressWarnings("unchecked")
	private <M> M loadExpectedModel(String name) {
		if(folder == null && folder.length() != 0) {
			Logger.getLogger("BenchmarxUtil").log(Level.SEVERE, "Please set a foldername for this test!");
			throw new RuntimeException();
		}
		
		
		ResourceSet resourceSet = new ResourceSetImpl();
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put(Resource.Factory.Registry.DEFAULT_EXTENSION, new XMIResourceFactoryImpl());
	      
		Resource resource = resourceSet.createResource(URI.createFileURI("resources/" + folder + "/" + name + ".xmi"));
		try {
			resource.load(null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return (M)resource.getContents().get(0);
	}
	
	public void setFolder(String folderName)  {
		folder = folderName;
	}
	
	public void assertPrecondition(String srcPath, String trgPath){
		tool.assertPrecondition(loadExpectedModel(srcPath), loadExpectedModel(trgPath));
	}
	
	public void assertPostcondition(String srcPath, String trgPath){
		tool.assertPostcondition(loadExpectedModel(srcPath), loadExpectedModel(trgPath));
	}
	
	public Configurator<D> configure() {
		Configurator<D> c = new Configurator<>();
		tool.setConfigurator(c);
		return c;
	}
	
	public <X> Consumer<X> execute(Consumer<X> a){
		return a;
	}
}
