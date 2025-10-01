<script lang="ts">
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
				<Breadcrumb.Item class="hidden md:block">
					<Breadcrumb.Link href="/">Главная</Breadcrumb.Link>
				</Breadcrumb.Item>
				<Breadcrumb.Separator class="hidden md:block" />
				<Breadcrumb.Item>
					<Breadcrumb.Page>Предметная область</Breadcrumb.Page>
				</Breadcrumb.Item>
			</Breadcrumb.List>
		</Breadcrumb.Root>
	</div>
</header>

<div class="container mx-auto max-w-4xl p-6">
	<div class="mb-8">
		<h1 class="text-3xl font-bold text-primary">Описание предметной области</h1>
		<p class="mt-2 text-lg text-muted-foreground">
			Предметная область информационной системы посвящена управлению данными об организациях. Ниже
			описаны ключевые сущности и их характеристики, используемые в системе.
		</p>
	</div>

	<div class="mb-8">
		<h2 class="text-2xl font-semibold text-primary">Организация (Organization)</h2>
		<p class="mt-4 text-base text-foreground">
			Сущность <code class="rounded bg-muted px-1">Organization</code> представляет организацию с набором
			характеристик:
		</p>
		<ul class="mt-4 list-disc space-y-2 pl-5 text-base text-foreground">
			<li>
				<strong>Идентификатор</strong>: Уникальный номер, автоматически генерируемый, всегда
				положительный и не может быть пустым.
			</li>
			<li>
				<strong>Название</strong>: Обязательное поле, содержащее непустое название организации.
			</li>
			<li>
				<strong>Координаты</strong>: Обязательная информация о местоположении, связанная с сущностью
				координат.
			</li>
			<li>
				<strong>Дата создания</strong>: Автоматически генерируемая дата, не может быть пустой.
			</li>
			<li>
				<strong>Официальный адрес</strong>: Необязательный адрес, связанный с сущностью адреса.
			</li>
			<li>
				<strong>Годовой оборот</strong>: Положительное значение, указывающее финансовый оборот
				организации.
			</li>
			<li>
				<strong>Количество сотрудников</strong>: Положительное число, отражающее численность
				персонала.
			</li>
			<li>
				<strong>Рейтинг</strong>: Обязательное положительное значение, характеризующее рейтинг
				организации.
			</li>
			<li>
				<strong>Тип организации</strong>: Необязательное поле, указывающее тип организации
				(например, коммерческая, государственная).
			</li>
			<li>
				<strong>Почтовый адрес</strong>: Необязательный адрес для корреспонденции, связанный с
				сущностью адреса.
			</li>
		</ul>
	</div>

	<div class="mb-8">
		<h2 class="text-2xl font-semibold text-primary">Координаты (Coordinates)</h2>
		<p class="mt-4 text-base text-foreground">
			Сущность <code class="rounded bg-muted px-1">Coordinates</code> описывает географическое положение:
		</p>
		<ul class="mt-4 list-disc space-y-2 pl-5 text-base text-foreground">
			<li><strong>X</strong>: Координата по оси X, должна быть больше -469.</li>
			<li><strong>Y</strong>: Обязательная координата по оси Y, должна быть больше -440.</li>
		</ul>
	</div>

	<div class="mb-8">
		<h2 class="text-2xl font-semibold text-primary">Адрес (Address)</h2>
		<p class="mt-4 text-base text-foreground">
			Сущность <code class="rounded bg-muted px-1">Address</code> содержит информацию об адресе:
		</p>
		<ul class="mt-4 list-disc space-y-2 pl-5 text-base text-foreground">
			<li><strong>Улица</strong>: Обязательное поле с названием улицы.</li>
			<li>
				<strong>Почтовый индекс</strong>: Необязательное поле, длина которого не превышает 24
				символа.
			</li>
			<li>
				<strong>Город</strong>: Обязательная информация о городе, связанная с сущностью
				местоположения.
			</li>
		</ul>
	</div>

	<div class="mb-8">
		<h2 class="text-2xl font-semibold text-primary">Местоположение (Location)</h2>
		<p class="mt-4 text-base text-foreground">
			Сущность <code class="rounded bg-muted px-1">Location</code> описывает конкретное местоположение:
		</p>
		<ul class="mt-4 list-disc space-y-2 pl-5 text-base text-foreground">
			<li><strong>X</strong>: Координата по оси X.</li>
			<li><strong>Y</strong>: Координата по оси Y.</li>
			<li><strong>Z</strong>: Обязательная координата по оси Z.</li>
		</ul>
	</div>

	<div class="mb-8">
		<h2 class="text-2xl font-semibold text-primary">Тип организации (OrganizationType)</h2>
		<p class="mt-4 text-base text-foreground">
			Сущность <code class="rounded bg-muted px-1">OrganizationType</code> определяет возможные типы
			организаций:
		</p>
		<ul class="mt-4 list-disc space-y-2 pl-5 text-base text-foreground">
			<li>Коммерческая (COMMERCIAL)</li>
			<li>Публичная (PUBLIC)</li>
			<li>Государственная (GOVERNMENT)</li>
			<li>Трастовая (TRUST)</li>
			<li>Открытая акционерная компания (OPEN_JOINT_STOCK_COMPANY)</li>
		</ul>
	</div>
</div>
