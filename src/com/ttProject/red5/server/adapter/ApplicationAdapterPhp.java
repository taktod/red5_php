package com.ttProject.red5.server.adapter;

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
import com.ttProject.red5.server.adapter.library.ArgumentManager;

/**
 * Application adapter for PHP usage.
 */
public class ApplicationAdapterPhp extends ApplicationAdapter {
	private QuercusEx quercus;
	public ApplicationAdapterPhp() {
		quercus = new QuercusEx();
	}
	@Override
	public boolean appConnect(IConnection conn, Object[] params) {
		if(!super.appConnect(conn, params)) {
			return false;
		}
		ArgumentManager args = ArgumentManager.getInstance();
		String key = args.setArgument(this, conn, params);
		try {
			quercus.execute("webapps/" + this.getName() + "/WEB-INF/php/appConnect.php", key);
		}
		catch(IOException e) {
			e.printStackTrace();
			return false;
		}
		Object retval = args.getRetval(key);
		if(retval instanceof Boolean) {
			return (Boolean)retval;
		}
		return false;
	}
	@Override
	public void appDisconnect(IConnection conn) {
		ArgumentManager args = ArgumentManager.getInstance();
		try {
			quercus.execute("webapps/" + this.getName() + "/WEB-INF/php/appDisconnect.php", args.setArgument(this, conn));
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		super.appDisconnect(conn);
	}
	@Override
	public boolean appJoin(IClient client, IScope scope) {
		if(!super.appJoin(client, scope)) {
			return false;
		}
		ArgumentManager args = ArgumentManager.getInstance();
		String key = args.setArgument(this, client, scope);
		try {
			quercus.execute("webapps/" + this.getName() + "/WEB-INF/php/appJoin.php", key);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		Object retval = args.getRetval(key);
		if(retval instanceof Boolean) {
			return (Boolean)retval;
		}
		return false;
	}
	@Override
	public void appLeave(IClient client, IScope scope) {
		ArgumentManager args = ArgumentManager.getInstance();
		try {
			quercus.execute("webapps/" + this.getName() + "/WEB-INF/php/appLeave.php", args.setArgument(this, client, scope));
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		super.appLeave(client, scope);
	}
	@Override
	public boolean appStart(IScope scope) {
		if(!super.appStart(scope)) {
			return false;
		}
		ArgumentManager args = ArgumentManager.getInstance();
		String key = args.setArgument(this, scope);
		try {
			quercus.execute("webapps/" + this.getName() + "/WEB-INF/php/appStart.php", key);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Object retval = args.getRetval(key);
		if(retval instanceof Boolean) {
			return (Boolean)retval;
		}
		return false;
	}
	@Override
	public void appStop(IScope scope) {
		ArgumentManager args = ArgumentManager.getInstance();
		try {
			quercus.execute("webapps/" + this.getName() + "/WEB-INF/php/appStop.php", args.setArgument(this, scope));
		} catch (IOException e) {
			e.printStackTrace();
		}
		super.appStop(scope);
	}
	@Override
	public synchronized boolean connect(IConnection conn, IScope scope,
			Object[] params) {
		if(!super.connect(conn, scope, params)) {
			return false;
		}
		ArgumentManager args = ArgumentManager.getInstance();
		String key = args.setArgument(this, conn, scope, params);
		try {
			quercus.execute("webapps/" + this.getName() + "/WEB-INF/php/connect.php", key);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Object retval = args.getRetval(key);
		if(retval instanceof Boolean) {
			return (Boolean)retval;
		}
		return false;
	}
	@Override
	public synchronized void disconnect(IConnection conn, IScope scope) {
		ArgumentManager args = ArgumentManager.getInstance();
		try {
			quercus.execute("webapps/" + this.getName() + "/WEB-INF/php/disconnect.php", args.setArgument(this, conn, scope));
		} catch (IOException e) {
			e.printStackTrace();
		}
		super.disconnect(conn, scope);
	}
	@Override
	public synchronized boolean join(IClient client, IScope scope) {
		if(!super.join(client, scope)) {
			return false;
		}
		ArgumentManager args = ArgumentManager.getInstance();
		String key = args.setArgument(this, client, scope);
		try {
			quercus.execute("webapps/" + this.getName() + "/WEB-INF/php/join.php", key);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Object retval = args.getRetval(key);
		if(retval instanceof Boolean) {
			return (Boolean)retval;
		}
		return false;
	}
	@Override
	public synchronized void leave(IClient client, IScope scope) {
		ArgumentManager args = ArgumentManager.getInstance();
		try {
			quercus.execute("webapps/" + this.getName() + "/WEB-INF/php/leave.php", args.setArgument(this, client, scope));
		} catch (IOException e) {
			e.printStackTrace();
		}
		super.leave(client, scope);
	}
	@Override
	public synchronized boolean start(IScope scope) {
		if(!super.start(scope)) {
			return false;
		}
		ArgumentManager args = ArgumentManager.getInstance();
		String key = args.setArgument(this, scope);
		try {
			quercus.execute("webapps/" + this.getName() + "/WEB-INF/php/start.php", key);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Object retval = args.getRetval(key);
		if(retval instanceof Boolean) {
			return (Boolean)retval;
		}
		return false;
	}
	@Override
	public synchronized void stop(IScope scope) {
		ArgumentManager args = ArgumentManager.getInstance();
		try {
			quercus.execute("webapps/" + this.getName() + "/WEB-INF/php/stop.php", args.setArgument(this, scope));
		} catch (IOException e) {
			e.printStackTrace();
		}
		super.stop(scope);
	}
	@Override
	public boolean roomConnect(IConnection conn, Object[] params) {
		if(!super.roomConnect(conn, params)) {
			return false;
		}
		ArgumentManager args = ArgumentManager.getInstance();
		String key = args.setArgument(this, conn, params);
		try {
			quercus.execute("webapps/" + this.getName() + "/WEB-INF/php/roomConnect.php", key);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Object retval = args.getRetval(key);
		if(retval instanceof Boolean) {
			return (Boolean)retval;
		}
		return false;
	}
	@Override
	public void roomDisconnect(IConnection conn) {
		ArgumentManager args = ArgumentManager.getInstance();
		try {
			quercus.execute("webapps/" + this.getName() + "/WEB-INF/php/roomDisconnect.php", args.setArgument(this, conn));
		} catch (IOException e) {
			e.printStackTrace();
		}
		super.roomDisconnect(conn);
	}
	@Override
	public boolean roomJoin(IClient client, IScope scope) {
		if(!super.roomJoin(client, scope)) {
			return false;
		}
		ArgumentManager args = ArgumentManager.getInstance();
		String key = args.setArgument(this, client, scope);
		try {
			quercus.execute("webapps/" + this.getName() + "/WEB-INF/php/roomJoin.php", key);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Object retval = args.getRetval(key);
		if(retval instanceof Boolean) {
			return (Boolean)retval;
		}
		return false;
	}
	@Override
	public void roomLeave(IClient client, IScope scope) {
		ArgumentManager args = ArgumentManager.getInstance();
		try {
			quercus.execute("webapps/" + this.getName() + "/WEB-INF/php/roomLeave.php", args.setArgument(this, client, scope));
		} catch (IOException e) {
			e.printStackTrace();
		}
		super.roomLeave(client, scope);
	}
	@Override
	public boolean roomStart(IScope scope) {
		if(!super.roomStart(scope)) {
			return false;
		}
		ArgumentManager args = ArgumentManager.getInstance();
		String key = args.setArgument(this, scope);
		try {
			quercus.execute("webapps/" + this.getName() + "/WEB-INF/php/roomStart.php", key);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Object retval = args.getRetval(key);
		if(retval instanceof Boolean) {
			return (Boolean)retval;
		}
		return false;
	}
	@Override
	public void roomStop(IScope scope) {
		ArgumentManager args = ArgumentManager.getInstance();
		try {
			quercus.execute("webapps/" + this.getName() + "/WEB-INF/php/roomStop.php", args.setArgument(this, scope));
		} catch (IOException e) {
			e.printStackTrace();
		}
		super.roomStop(scope);
	}
	@Override
	public void streamBroadcastStart(IBroadcastStream stream) {
		super.streamBroadcastStart(stream);
		ArgumentManager args = ArgumentManager.getInstance();
		try {
			quercus.execute("webapps/" + this.getName() + "/WEB-INF/php/streamBroadcastStart.php", args.setArgument(this, stream));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void streamBroadcastClose(IBroadcastStream stream) {
		ArgumentManager args = ArgumentManager.getInstance();
		try {
			quercus.execute("webapps/" + this.getName() + "/WEB-INF/php/streamBroadcastClose.php", args.setArgument(this, stream));
		} catch (IOException e) {
			e.printStackTrace();
		}
		super.streamBroadcastClose(stream);
	}
	@Override
	public void streamPublishStart(IBroadcastStream stream) {
		super.streamPublishStart(stream);
		ArgumentManager args = ArgumentManager.getInstance();
		try {
			quercus.execute("webapps/" + this.getName() + "/WEB-INF/php/streamPublishStart.php", args.setArgument(this, stream));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void streamRecordStart(IBroadcastStream stream) {
		super.streamRecordStart(stream);
		ArgumentManager args = ArgumentManager.getInstance();
		try {
			quercus.execute("webapps/" + this.getName() + "/WEB-INF/php/streamRecordStart.php", args.setArgument(this, stream));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * private quercus class for php engine.
	 */
	private class QuercusEx extends Quercus {
		private String arg = "";
		public QuercusEx() {
			super();
			this.init();
			this.start();
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

		    Env env = createEnv(page, os, null, null);
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