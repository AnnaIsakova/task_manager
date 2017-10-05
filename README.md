<h2><strong>A web application (task manager) with Spring Boot on back-end and AngularJS on front-end.</strong></h2>

<h4>Live: http://task.annaisakova.me</h4>
<p>For testing you can use login: team@team.com and pass: 11111</p>
<hr>
<p>
  While registring a user can choose a role.<br/>
  Roles available: team leader, developer.
</p>
<p>
  Team leader can:
  <ul>
    <li>create/update/delete projects;</li>
    <li>add/delete files to projects and tasks;</li>
    <li>invite/delete developers into a project;</li>
    <li>create/update/delete tasks for projects;</li>
    <li>add/edit/delete comments for a project and tasks.</li>
  </ul>
</p>
<p>
  Developer can:
  <ul>
    <li>download files from projects and tasks;</li>
    <li>change task's status (i.e. new, in progress, complete);</li>
    <li>add/edit/delete comments for a project and tasks.</li>
  </ul>
</p>
<p>
  Also everyone has his own todo list and all permissions to create/update/delete todo tasks.<br/>
  Front-end part also includes calendar and charts. These sections does not require some additional back-end logic and needs some fixing.
</p>
