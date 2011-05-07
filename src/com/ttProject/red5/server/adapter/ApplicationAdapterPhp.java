package com.ttProject.red5.server.adapter;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import org.red5.server.adapter.ApplicationAdapter;
import org.red5.server.api.IClient;
import org.red5.server.api.IConnection;
import org.red5.server.api.IScope;
import org.red5.server.api.stream.IBroadcastStream;

import com.caucho.quercus.Quercus;
import com.caucho.quercus.QuercusDieException;
import com.caucho.quercus.QuercusErrorException;
import com.caucho.quercus.QuercusExitException;
import com.caucho.quercus.env.Env;
import com.caucho.quercus.page.QuercusPage;
import com.caucho.vfs.Path;
import com.caucho.vfs.StdoutStream;
import com.caucho.vfs.WriteStream;
import com.ttProject.red5.server.adapter.library.php.ArgumentManager;

/**
 * Application adapter for PHP usage.
 */
public class ApplicationAdapterPhp extends ApplicationAdapter implements ActionListener{
	private QuercusEx quercus;
	private String directory;
	public ApplicationAdapterPhp() {
		quercus = new QuercusEx();
		directory = null;
	}
	/**
	 * @param directory the directory to set
	 */
	public void setDirectory(String directory) {
		this.directory = directory;
	}
	/**
	 * @return the env
	 */
	public Env getEnv() {
		return quercus.getEnv();
	}
	@Override
	public boolean appConnect(IConnection conn, Object[] params) {
		if(!super.appConnect(conn, params)) {
			return false;
		}
		Object retval = execute("appConnect.php", this, conn, params);
		if(retval instanceof Boolean) {
			return (Boolean)retval;
		}
		return false;
	}
	@Override
	public void appDisconnect(IConnection conn) {
		execute("appDisconnect.php", this, conn);
		super.appDisconnect(conn);
	}
	@Override
	public boolean appJoin(IClient client, IScope scope) {
		if(!super.appJoin(client, scope)) {
			return false;
		}
		Object retval = execute("appJoin.php", this, client, scope);
		if(retval instanceof Boolean) {
			return (Boolean)retval;
		}
		return false;
	}
	@Override
	public void appLeave(IClient client, IScope scope) {
		execute("appLeave.php", this, client, scope);
		super.appLeave(client, scope);
	}
	@Override
	public boolean appStart(IScope scope) {
		if(!super.appStart(scope)) {
			return false;
		}
		Object retval = execute("appStart.php", this, scope);
		if(retval instanceof Boolean) {
			return (Boolean)retval;
		}
		return false;
	}
	@Override
	public void appStop(IScope scope) {
		execute("appStop.php", this, scope);
		super.appStop(scope);
	}
	@Override
	public synchronized boolean connect(IConnection conn, IScope scope,
			Object[] params) {
		if(!super.connect(conn, scope, params)) {
			return false;
		}
		Object retval = execute("connect.php", this, conn, scope);
		if(retval instanceof Boolean) {
			return (Boolean)retval;
		}
		return false;
	}
	@Override
	public synchronized void disconnect(IConnection conn, IScope scope) {
		execute("disconnect.php", this, conn, scope);
		super.disconnect(conn, scope);
	}
	@Override
	public synchronized boolean join(IClient client, IScope scope) {
		if(!super.join(client, scope)) {
			return false;
		}
		Object retval = execute("join.php", this, client, scope);
		if(retval instanceof Boolean) {
			return (Boolean)retval;
		}
		return false;
	}
	@Override
	public synchronized void leave(IClient client, IScope scope) {
		execute("leave.php", this, client, scope);
		super.leave(client, scope);
	}
	@Override
	public synchronized boolean start(IScope scope) {
		if(!super.start(scope)) {
			return false;
		}
		Object retval = execute("start.php", this, scope);
		if(retval instanceof Boolean) {
			return (Boolean)retval;
		}
		return false;
	}
	@Override
	public synchronized void stop(IScope scope) {
		execute("stop.php", this.scope);
		super.stop(scope);
	}
	@Override
	public boolean roomConnect(IConnection conn, Object[] params) {
		if(!super.roomConnect(conn, params)) {
			return false;
		}
		Object retval = execute("roomConnect.php", this, conn, params);
		if(retval instanceof Boolean) {
			return (Boolean)retval;
		}
		return false;
	}
	@Override
	public void roomDisconnect(IConnection conn) {
		execute("roomDisconnect.php", this, conn);
		super.roomDisconnect(conn);
	}
	@Override
	public boolean roomJoin(IClient client, IScope scope) {
		if(!super.roomJoin(client, scope)) {
			return false;
		}
		Object retval = execute("roomJoin.php", this, client, scope);
		if(retval instanceof Boolean) {
			return (Boolean)retval;
		}
		return false;
	}
	@Override
	public void roomLeave(IClient client, IScope scope) {
		execute("roomLeave.php", this, client, scope);
		super.roomLeave(client, scope);
	}
	@Override
	public boolean roomStart(IScope scope) {
		if(!super.roomStart(scope)) {
			return false;
		}
		Object retval = execute("roomStart.php", this, scope);
		if(retval instanceof Boolean) {
			return (Boolean)retval;
		}
		return false;
	}
	@Override
	public void roomStop(IScope scope) {
		execute("roomStop.php", this, scope);
		super.roomStop(scope);
	}
	@Override
	public void streamBroadcastStart(IBroadcastStream stream) {
		super.streamBroadcastStart(stream);
		execute("streamBroadcastStart.php", this, stream);
	}
	@Override
	public void streamBroadcastClose(IBroadcastStream stream) {
		execute("streamBroadcastClose.php", this, stream);
		super.streamBroadcastClose(stream);
	}
	@Override
	public void streamPublishStart(IBroadcastStream stream) {
		super.streamPublishStart(stream);
		execute("streamPublishStart.php", this, stream);
	}
	@Override
	public void streamRecordStart(IBroadcastStream stream) {
		super.streamRecordStart(stream);
		execute("streamRecordStart.php", this, stream);
	}
	@Override
	public void actionPerformed(ActionEvent event) {
		execute("actionEvent.php", this, event);
	}
	protected Object execute(String phpfile, Object ... params) {
		ArgumentManager args = ArgumentManager.getInstance();
		String key = args.setArgument(params);
		try {
			quercus.execute(directory + "/php/" + phpfile, key);
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		return args.getRetval(key);
	}
	/**
	 * private quercus class for php engine.
	 */
	private class QuercusEx extends Quercus {
		private Env env;
		private String arg = "";
		public QuercusEx() {
			super();
			this.init();
			this.start();
		}
		
		/**
		 * @return the env
		 */
		public Env getEnv() {
			return env;
		}

		public void execute(String path, String arg)
				throws IOException{
			this.setFileName(path);
			this.arg = arg;
			this.execute();
		}
		@Override
		public void execute(Path path) 
				throws IOException {
			QuercusPage page = parse(path);
		    WriteStream os = new WriteStream(StdoutStream.create());

		    os.setNewlineString("\n");
		    os.setEncoding("UTF-8");

		    env = createEnv(page, os, null, null);
		    env.setGlobalValue("_JAVAARG", objectToValue(arg));
		    env.start();

		    try {
		      env.execute();
		    } catch (QuercusDieException e) {
		    } catch (QuercusExitException e) {
		    } catch (QuercusErrorException e) {
		    } finally {
		      env.close();

		      os.flush();
		    }
		}
	}
}