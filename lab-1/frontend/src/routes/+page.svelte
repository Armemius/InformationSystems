<script>
	import * as Breadcrumb from '$lib/components/ui/breadcrumb/index.js';
	import { Separator } from '$lib/components/ui/separator/index.js';
	import * as Sidebar from '$lib/components/ui/sidebar/index.js';
</script>

<header
	class="flex h-16 shrink-0 items-center gap-2 transition-[width,height] ease-linear group-has-data-[collapsible=icon]/sidebar-wrapper:h-12"
>
	<div class="flex items-center gap-2 px-4">
		<Sidebar.Trigger class="-ml-1" />
		<Separator orientation="vertical" class="mr-2 data-[orientation=vertical]:h-4" />
		<Breadcrumb.Root>
			<Breadcrumb.List>
				<Breadcrumb.Item>
					<Breadcrumb.Page>Главная</Breadcrumb.Page>
				</Breadcrumb.Item>
			</Breadcrumb.List>
		</Breadcrumb.Root>
	</div>
</header>

<div class="container mx-auto max-w-4xl p-6">
	<div class="mb-8">
		<h1 class="text-3xl font-bold text-primary">Лабораторная работа №1</h1>
		<p class="mt-2 text-lg text-muted-foreground">Степанов Арсений Алексеевич</p>
	</div>

	<div class="mb-8">
		<h2 class="text-2xl font-semibold text-primary">Описание проекта</h2>
		<p class="mt-4 text-base text-foreground">
			Цель проекта — разработка информационной системы для управления объектами класса <code
				class="rounded bg-muted px-1">Organization</code
			>. Система позволяет создавать, просматривать, обновлять и удалять объекты, обеспечивая
			взаимодействие с базой данных PostgreSQL через серверную часть. Основные функции и требования
			описаны ниже.
		</p>
	</div>

	<div class="mb-8">
		<h2 class="text-2xl font-semibold text-primary">Основные требования к системе</h2>
		<ul class="mt-4 list-disc space-y-2 pl-5 text-base text-foreground">
			<li>
				Создание, получение, обновление и удаление объектов <code class="rounded bg-muted px-1"
					>Organization</code
				> в отдельных интерфейсах.
			</li>
			<li>Отображение связанных объектов при просмотре информации об объекте.</li>
			<li>Связывание новых объектов с существующими объектами вспомогательных классов.</li>
			<li>Управление объектами на серверной стороне с синхронизацией изменений в базе данных.</li>
			<li>Отображение списка объектов в виде таблицы с пагинацией.</li>
			<li>Фильтрация и сортировка таблицы по строковым полям с точным совпадением.</li>
			<li>Переход к редактированию объекта из таблицы или области визуализации.</li>
			<li>
				Автоматическое обновление интерфейса у всех пользователей при добавлении, удалении или
				изменении объектов.
			</li>
			<li>Каскадное удаление связанных объектов.</li>
			<li>
				Просмотр объектов всеми пользователями, редактирование в диалоговом окне с информативными
				сообщениями об ошибках.
			</li>
		</ul>
	</div>

	<div class="mb-8">
		<h2 class="text-2xl font-semibold text-primary">Специальные операции</h2>
		<ul class="mt-4 list-disc space-y-2 pl-5 text-base text-foreground">
			<li>
				Поиск объектов, у которых поле <code class="rounded bg-muted px-1">name</code> начинается с заданной
				подстроки.
			</li>
			<li>
				Фильтрация объектов по значению поля <code class="rounded bg-muted px-1">type</code> (меньше
				или больше заданного).
			</li>
			<li>Объединение организаций: создание новой организации с сотрудниками двух исходных.</li>
			<li>Поглощение одной организации другой без увольнения сотрудников.</li>
		</ul>
		<p class="mt-4 text-sm text-muted-foreground">
			Все операции реализуются в бизнес-логике приложения без прямого использования функций базы
			данных.
		</p>
	</div>

	<div class="mb-8">
		<h2 class="text-2xl font-semibold text-primary">Хранение данных</h2>
		<ul class="mt-4 list-disc space-y-2 pl-5 text-base text-foreground">
			<li>
				Хранение объектов в реляционной СУБД PostgreSQL (база данных: <code
					class="rounded bg-muted px-1">studs</code
				>, хост: <code class="rounded bg-muted px-1">pg</code>).
			</li>
			<li>Соответствие требованиям к полям классов через ORM и ограничения базы данных.</li>
			<li>
				Автоматическая генерация поля <code class="rounded bg-muted px-1">id</code> с использованием
				средств базы данных.
			</li>
		</ul>
	</div>

	<div class="mb-8">
		<h2 class="text-2xl font-semibold text-primary">Интерфейс и взаимодействие</h2>
		<ul class="mt-4 list-disc space-y-2 pl-5 text-base text-foreground">
			<li>
				Обработка некорректного ввода с ограничением недопустимых значений и сообщениями об ошибках.
			</li>
			<li>Навигация между частями системы через меню.</li>
			<li>Автоматическое обновление интерфейса у всех клиентов при изменениях объектов.</li>
		</ul>
	</div>

	<div>
		<h2 class="text-2xl font-semibold text-primary">Технические требования</h2>
		<ul class="mt-4 list-disc space-y-2 pl-5 text-base text-foreground">
			<li>Использование <strong>Spring MVC</strong> для реализации системы.</li>
			<li>Применение <strong>Hibernate</strong> для уровня хранения данных.</li>
			<li>Разделение уровней приложения и логических компонентов.</li>
		</ul>
	</div>
</div>
